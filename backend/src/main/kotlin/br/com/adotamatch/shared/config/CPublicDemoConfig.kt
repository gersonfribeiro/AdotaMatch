package br.com.adotamatch.shared.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @description Configura a navegação e as operações públicas do protótipo acadêmico.
 */
@Configuration
@Profile("demo")
class CPublicDemoConfig : WebMvcConfigurer {

    /**
     * @description Encaminha as rotas públicas da SPA para o arquivo inicial.
     * @param pRegistry Registro de rotas MVC.
     */
    override fun addViewControllers(pRegistry: ViewControllerRegistry) {
        pRegistry.addViewController("/match").setViewName("forward:/index.html")
        pRegistry.addViewController("/pets").setViewName("forward:/index.html")
    }

    /**
     * @description Autoriza a leitura do catálogo e o cálculo de recomendações na demonstração.
     * @param pRegistry Registro de interceptadores MVC.
     */
    override fun addInterceptors(pRegistry: InterceptorRegistry) {
        pRegistry.addInterceptor(object : HandlerInterceptor {
            override fun preHandle(
                pRequest: HttpServletRequest,
                pResponse: HttpServletResponse,
                pHandler: Any
            ): Boolean {
                val path = pRequest.requestURI.removePrefix(pRequest.contextPath)
                val allowed = when (pRequest.method) {
                    "GET", "HEAD" -> CATALOG_PATH.matches(path)
                    "POST" -> path == "/api/v1/recommendations/match"
                    else -> false
                }
                if (!allowed) {
                    pResponse.sendError(HttpServletResponse.SC_NOT_FOUND)
                }
                return allowed
            }
        }).addPathPatterns("/api/**")
    }

    private companion object {
        val CATALOG_PATH = Regex("/api/v1/pets(?:/[0-9]+)?")
    }
}
