package tw.edu.fju.miniclinic.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("loggedInDoctorId") == null) {
            String path = request.getRequestURI();
            if (path.startsWith("/api/")) {
                response.setStatus(401);
                response.getWriter().write("Unauthenticated");
            } else {
                response.sendRedirect("/login");
            }
            return false;
        }
        return true;
    }
}