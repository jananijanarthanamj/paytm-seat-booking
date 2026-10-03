package paytm.com.example.Security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticatedUserFilter extends OncePerRequestFilter {

	public static final String USER_ID_ATTRIBUTE = "authenticatedUserId";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Authorization token is required");
			return;
		}

		String token = authorization.substring(7).trim();

		if (token.isBlank()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid authorization token");
			return;
		}

		// For this assignment, the token represents the authenticated user.
		request.setAttribute(USER_ID_ATTRIBUTE, token);

		filterChain.doFilter(request, response);
	}
}