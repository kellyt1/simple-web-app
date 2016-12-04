package org.mitre.web;

import java.security.Principal;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.mitre.openid.connect.client.OIDCAuthenticationFilter;
import org.mitre.openid.connect.client.SubjectIssuerGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Application {
    ApplicationContext context = new ClassPathXmlApplicationContext("servlet-context.xml");
    
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
        System.out.println("app starteds");
    }
}


@Controller
 class HomeTestController {

	//private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// filter reference so we can get class names and things like that.
	@Autowired
	private OIDCAuthenticationFilter filter;
	
	@Resource(name = "namedAdmins")
	private Set<SubjectIssuerGrantedAuthority> admins;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, Principal p) {

		model.addAttribute("issuerServiceClass", filter.getIssuerService().getClass().getSimpleName());
		model.addAttribute("serverConfigurationServiceClass", filter.getServerConfigurationService().getClass().getSimpleName());
		model.addAttribute("clientConfigurationServiceClass", filter.getClientConfigurationService().getClass().getSimpleName());
		model.addAttribute("authRequestOptionsServiceClass", filter.getAuthRequestOptionsService().getClass().getSimpleName());
		model.addAttribute("authRequestUriBuilderClass", filter.getAuthRequestUrlBuilder().getClass().getSimpleName());
		
		model.addAttribute("admins", admins);

		return "home";
	}

	@RequestMapping("/user")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String user(Principal p) {
		return "user";
	}

	@RequestMapping("/open")
	public String open(Principal p) {
		return "open";
	}

	@RequestMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String admin(Model model, Principal p) {

		model.addAttribute("admins", admins);

		return "admin";
	}

	@RequestMapping("/login")
	public String login(Principal p) {
		return "login";
	}

}