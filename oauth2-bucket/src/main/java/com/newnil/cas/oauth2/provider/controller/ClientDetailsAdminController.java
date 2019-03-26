package com.newnil.cas.oauth2.provider.controller;

import com.newnil.cas.oauth2.provider.dao.entity.ClientDetailsToScopesXrefEntity;
import com.newnil.cas.oauth2.provider.dao.entity.RedirectUriEntity;
import com.newnil.cas.oauth2.provider.dao.repository.ClientDetailsRepository;
import com.newnil.cas.oauth2.provider.dao.repository.GrantTypeRepository;
import com.newnil.cas.oauth2.provider.dao.repository.ResourceIdRepository;
import com.newnil.cas.oauth2.provider.dao.repository.ScopeRepository;
import com.newnil.cas.oauth2.provider.service.OAuth2DatabaseClientDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.newnil.cas.oauth2.provider.webhelper.RedirectMessageHelper.*;

@Slf4j
@Controller
@RequestMapping("/clientDetails")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ClientDetailsAdminController {

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Autowired
    private GrantTypeRepository grantTypeRepository;

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private ResourceIdRepository resourceIdRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OAuth2DatabaseClientDetailsService clientDetailsService;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String listAll(@RequestParam(name = "edit", required = false) String editClientDetails, Model model, Pageable pageable) {

        if (!StringUtils.isEmpty(editClientDetails)) {
            clientDetailsRepository.findOneByClientId(editClientDetails).map(clientDetailsEntity -> {

                model.addAttribute("clientId", clientDetailsEntity.getClientId());
                model.addAttribute("accessTokenValiditySeconds", clientDetailsEntity.getAccessTokenValiditySeconds());
                model.addAttribute("refreshTokenValiditySeconds", clientDetailsEntity.getRefreshTokenValiditySeconds());
                model.addAttribute("selectedGrantTypes", clientDetailsEntity.getAuthorizedGrantTypeXrefs().stream().map(
                        xref -> xref.getGrantType().getValue()
                ).collect(Collectors.toList()));
                model.addAttribute("selectedScopes", clientDetailsEntity.getScopeXrefs().stream().map(
                        xref -> xref.getScope().getValue()
                ).collect(Collectors.toList()));
                model.addAttribute("selectedAutoApproveScopes", clientDetailsEntity.getScopeXrefs().stream()
                        .filter(ClientDetailsToScopesXrefEntity::getAutoApprove).map(
                                xref -> xref.getScope().getValue()
                        ).collect(Collectors.toList())
                );
                model.addAttribute("selectedResourceIds", clientDetailsEntity.getResourceIdXrefs().stream().map(
                        xref -> xref.getResourceId().getValue()
                ).collect(Collectors.toList()));
                model.addAttribute("redirectUris", clientDetailsEntity.getRedirectUris().stream()
                        .map(RedirectUriEntity::getValue).collect(Collectors.joining(System.lineSeparator()))
                );
                return null;
            });
        }

        model.addAttribute("clientDetailsList", clientDetailsRepository.findAll(pageable));
        model.addAttribute("grantTypes", grantTypeRepository.findAll());
        model.addAttribute("scopes", scopeRepository.findAll());
        model.addAttribute("resourceIds", resourceIdRepository.findAll());
        return "clients/clientDetails";
    }

    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final Pattern PASSWORD_WORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,}$");

    @RequestMapping(path = "/_create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String create(@RequestParam("clientId") String clientId,
                         @RequestParam("clientSecret") String clientSecret,
                         @RequestParam(name = "accessTokenValiditySeconds", required = false) Integer accessTokenValiditySeconds,
                         @RequestParam(name = "refreshTokenValiditySeconds", required = false) Integer refreshTokenValiditySeconds,
                         @RequestParam(name = "grantTypes", defaultValue = "") List<String> grantTypes,
                         @RequestParam(name = "scopes", defaultValue = "") List<String> scopes,
                         @RequestParam(name = "autoApproveAll", defaultValue = "false") boolean autoApproveAll,
                         @RequestParam(name = "autoApproveScopes", defaultValue = "") List<String> autoApproveScopes,
                         @RequestParam(name = "resourceIds", defaultValue = "") List<String> resourceIds,
                         @RequestParam("redirectUris") String redirectUris,
                         RedirectAttributes attributes) {

        if (!CLIENT_ID_PATTERN.matcher(clientId).matches()) {
            addErrorMessage(attributes, "客户端ID " + clientId + " 含有非法字符。（只能使用[a-zA-Z0-9_]）");
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        if (clientDetailsRepository.findOneByClientId(clientId).isPresent()) {
            addErrorMessage(attributes, "客户端ID " + clientId + " 已存在。");
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        if (!PASSWORD_WORD_PATTERN.matcher(clientSecret).matches()) {
            addErrorMessage(attributes, "客户端密码含有非法字符。（只能使用[a-zA-Z0-9]，至少6位）");
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        if (accessTokenValiditySeconds != null && accessTokenValiditySeconds < 0) {
            addErrorMessage(attributes, "AccessToken有效秒数不能小于零。");
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        if (refreshTokenValiditySeconds != null && refreshTokenValiditySeconds < 0) {
            addErrorMessage(attributes, "RefreshToken有效秒数不能小于零。");
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        // 检查授权方式
        if (!checkGrantTypeValidation(grantTypes, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        // 检查授权范围
        if (!checkScopeValidation(scopes, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        // 检查自动授权范围
        if (!checkScopeValidation(autoApproveScopes, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        // 检查资源ID
        if (!checkResourceIdValidation(resourceIds, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails";
        }

        Set<String> redirectUrisList = new HashSet<>();
        if (!StringUtils.isEmpty(redirectUris)) {
            LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(redirectUris));
            String line;
            try {
                while ((line = lineNumberReader.readLine()) != null) {
                    redirectUrisList.add(line);
                }
            } catch (IOException e) {
                log.warn("IOException while parsing redirect Uris: " + redirectUris, e);
            }
        }

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(clientId);
        baseClientDetails.setClientSecret(clientSecret);
        baseClientDetails.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        baseClientDetails.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        baseClientDetails.setAuthorizedGrantTypes(grantTypes);
        baseClientDetails.setScope(scopes);
        if (autoApproveAll) {
            baseClientDetails.setAutoApproveScopes(Collections.singleton("true"));
        } else {
            baseClientDetails.setAutoApproveScopes(autoApproveScopes);
        }
        baseClientDetails.setResourceIds(resourceIds);
        baseClientDetails.setRegisteredRedirectUri(redirectUrisList);

        clientDetailsService.addClientDetails(baseClientDetails);

        addSuccessMessage(attributes, "客户端 " + clientId + " 注册成功。");

        return "redirect:/clientDetails";
    }

    @RequestMapping(path = "/_update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String update(@RequestParam("clientId") String clientId,
                         @RequestParam(name = "clientSecret", required = false) String clientSecret,
                         @RequestParam(name = "accessTokenValiditySeconds", required = false) Integer accessTokenValiditySeconds,
                         @RequestParam(name = "refreshTokenValiditySeconds", required = false) Integer refreshTokenValiditySeconds,
                         @RequestParam(name = "grantTypes", defaultValue = "") List<String> grantTypes,
                         @RequestParam(name = "scopes", defaultValue = "") List<String> scopes,
                         @RequestParam(name = "autoApproveAll", defaultValue = "false") boolean autoApproveAll,
                         @RequestParam(name = "autoApproveScopes", defaultValue = "") List<String> autoApproveScopes,
                         @RequestParam(name = "resourceIds", defaultValue = "") List<String> resourceIds,
                         @RequestParam("redirectUris") String redirectUris,
                         RedirectAttributes attributes) {

        if (!clientDetailsRepository.findOneByClientId(clientId).isPresent()) {
            addErrorMessage(attributes, "找不到客户端ID " + clientId);
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails?edit=" + clientId;
        }

        if (!StringUtils.isEmpty(clientSecret)) {
            if (!PASSWORD_WORD_PATTERN.matcher(clientSecret).matches()) {
                addErrorMessage(attributes, "客户端密码含有非法字符。（只能使用[a-zA-Z0-9]，至少6位）");
                resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                        scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
                return "redirect:/clientDetails?edit=" + clientId;
            }
        }

        if (accessTokenValiditySeconds != null && accessTokenValiditySeconds < 0) {
            addErrorMessage(attributes, "AccessToken有效秒数不能小于零。");
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails?edit=" + clientId;
        }

        if (refreshTokenValiditySeconds != null && refreshTokenValiditySeconds < 0) {
            addErrorMessage(attributes, "RefreshToken有效秒数不能小于零。");
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails?edit=" + clientId;
        }

        // 检查授权方式
        if (!checkGrantTypeValidation(grantTypes, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails?edit=" + clientId;
        }

        // 检查授权范围
        if (!checkScopeValidation(scopes, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails?edit=" + clientId;
        }

        // 检查自动授权范围
        if (!checkScopeValidation(autoApproveScopes, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails?edit=" + clientId;
        }

        // 检查资源ID
        if (!checkResourceIdValidation(resourceIds, attributes)) {
            resetRequestParams(clientId, accessTokenValiditySeconds, refreshTokenValiditySeconds, grantTypes,
                    scopes, autoApproveAll, autoApproveScopes, resourceIds, redirectUris, attributes);
            return "redirect:/clientDetails?edit=" + clientId;
        }

        Set<String> redirectUrisList = new HashSet<>();
        if (!StringUtils.isEmpty(redirectUris)) {
            LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(redirectUris));
            String line;
            try {
                while ((line = lineNumberReader.readLine()) != null) {
                    redirectUrisList.add(line);
                }
            } catch (IOException e) {
                log.warn("IOException while parsing redirect Uris: " + redirectUris, e);
            }
        }

        BaseClientDetails baseClientDetails = (BaseClientDetails) clientDetailsService.loadClientByClientId(clientId);

        baseClientDetails.setClientId(clientId);
        baseClientDetails.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        baseClientDetails.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        baseClientDetails.setAuthorizedGrantTypes(grantTypes);
        baseClientDetails.setScope(scopes);
        if (autoApproveAll) {
            baseClientDetails.setAutoApproveScopes(Collections.singleton("true"));
        } else {
            baseClientDetails.setAutoApproveScopes(autoApproveScopes);
        }
        baseClientDetails.setResourceIds(resourceIds);
        baseClientDetails.setRegisteredRedirectUri(redirectUrisList);

        clientDetailsService.updateClientDetails(baseClientDetails);

        if (!StringUtils.isEmpty(clientSecret)) {
            clientDetailsService.updateClientSecret(clientId, clientSecret);
        }

        addSuccessMessage(attributes, "客户端 " + clientId + " 更新成功。");

        return "redirect:/clientDetails";
    }

    private boolean checkGrantTypeValidation(List<String> grantTypes, RedirectAttributes attributes) {
        List<String> invalidGrantTypes = new ArrayList<>();
        grantTypes.forEach(grantType -> {
            if (!grantTypeRepository.findOneByValue(grantType).isPresent()) {
                invalidGrantTypes.add(grantType);
            }
        });

        invalidGrantTypes.forEach(grantType -> addErrorMessage(attributes, "授权方式 " + grantType + " 无效。"));

        return invalidGrantTypes.isEmpty();
    }

    private boolean checkScopeValidation(List<String> scopes, RedirectAttributes attributes) {
        List<String> invalidScopes = new ArrayList<>();
        scopes.forEach(scope -> {
            if (!scopeRepository.findOneByValue(scope).isPresent()) {
                invalidScopes.add(scope);
            }
        });

        invalidScopes.forEach(scope -> addErrorMessage(attributes, "授权范围 " + scope + " 无效。"));

        return invalidScopes.isEmpty();
    }

    private boolean checkResourceIdValidation(List<String> resourceIds, RedirectAttributes attributes) {
        List<String> invalidResourceIds = new ArrayList<>();
        resourceIds.forEach(resourceId -> {
            if (!resourceIdRepository.findOneByValue(resourceId).isPresent()) {
                invalidResourceIds.add(resourceId);
            }
        });

        invalidResourceIds.forEach(resourceId -> addErrorMessage(attributes, "资源ID " + resourceId + " 无效。"));

        return invalidResourceIds.isEmpty();
    }


    private void resetRequestParams(String clientId, Integer accessTokenValiditySeconds, Integer refreshTokenValiditySeconds,
                                    List<String> grantTypes, List<String> scopes, boolean autoApproveAll, List<String> autoApproveScopes,
                                    List<String> resourceIds, String redirectUris,
                                    RedirectAttributes attributes) {

        attributes.addFlashAttribute("clientId", clientId);
        attributes.addFlashAttribute("accessTokenValiditySeconds", accessTokenValiditySeconds);
        attributes.addFlashAttribute("refreshTokenValiditySeconds", refreshTokenValiditySeconds);
        attributes.addFlashAttribute("selectedGrantTypes", grantTypes);
        attributes.addFlashAttribute("selectedScopes", scopes);
        attributes.addFlashAttribute("autoApproveAll", autoApproveAll);
        attributes.addFlashAttribute("selectedAutoApproveScopes", autoApproveScopes);
        attributes.addFlashAttribute("selectedResourceIds", resourceIds);
        attributes.addFlashAttribute("redirectUris", redirectUris);

    }

    @RequestMapping(path = "/_remove/{clientId}", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String delete(@PathVariable("clientId") String clientId, RedirectAttributes attributes) {

        try {
            clientDetailsService.removeClientDetails(clientId);
        } catch (NoSuchClientException e) {
            addWarningMessage(attributes, "没有找到客户端ID " + clientId + " 对应的客户端。");
        }

        return "redirect:/clientDetails";
    }


}
