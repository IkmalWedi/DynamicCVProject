package com.cvbuilder.controller;

import com.cvbuilder.model.CVBean;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Controller (MVC): receives requests, populates the CVBean,
 * validates it, and forwards to the proper JSP view.
 *
 * Mapped in WEB-INF/web.xml (no @WebServlet annotation is used).
 * File upload is enabled by <multipart-config> in web.xml.
 */
public class CVServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * GET flow: handles /CVServlet?action=sample
     * Generates a pre-filled sample CV without any user input.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("sample".equals(action)) {
            CVBean sample = createSampleBean();
            request.setAttribute("cvData", sample);
            request.setAttribute("isSample", Boolean.TRUE);
            forward(request, response, "/cvView.jsp");
        } else {
            forward(request, response, "/cvForm.jsp");
        }
    }

    /**
     * POST flow: processes the submitted CV form (multipart/form-data).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Must be called before reading any parameter
        request.setCharacterEncoding("UTF-8");

        // 1. Read the uploaded photo first (a file over the limit throws an exception)
        Part photoPart;
        try {
            photoPart = request.getPart("photo");
        } catch (IllegalStateException ex) {
            sendError(request, response,
                    "The uploaded photo is too large. Please choose an image under 2 MB.");
            return;
        }

        // 2. Extract text parameters and populate the bean using its setters
        CVBean cvBean = new CVBean();

        // Personal info
        cvBean.setFullName(request.getParameter("fullName"));
        cvBean.setProfessionalTitle(request.getParameter("professionalTitle"));
        cvBean.setEmail(request.getParameter("email"));
        cvBean.setPhone(request.getParameter("phone"));
        cvBean.setProfileUrl(request.getParameter("profileUrl"));
        cvBean.setProfileSummary(request.getParameter("profileSummary"));

        // Photo (optional): only set if the user actually chose a file
        if (photoPart != null && photoPart.getSize() > 0) {
            try (InputStream in = photoPart.getInputStream()) {
                byte[] data = in.readAllBytes();
                cvBean.setPhotoData(data, photoPart.getContentType());
            }
        }

        // Education
        cvBean.setHighestDegree(request.getParameter("highestDegree"));
        cvBean.setInstitution(request.getParameter("institution"));
        cvBean.setGraduationYear(request.getParameter("graduationYear"));
        cvBean.setMajor(request.getParameter("major"));

        // Experience
        cvBean.setJobTitle(request.getParameter("jobTitle"));
        cvBean.setCompany(request.getParameter("company"));
        cvBean.setDuration(request.getParameter("duration"));
        cvBean.setResponsibilities(request.getParameter("responsibilities"));

        // Skills
        cvBean.setTechnicalSkills(request.getParameter("technicalSkills"));
        cvBean.setSoftSkills(request.getParameter("softSkills"));

        // 3. Validate through the bean, then dispatch to the right view
        if (cvBean.validate()) {
            request.setAttribute("cvData", cvBean);
            forward(request, response, "/cvView.jsp");
        } else {
            request.setAttribute("errorMessage",
                    "Please correct the following problem(s) and submit again:");
            request.setAttribute("errorList", cvBean.getErrors());
            forward(request, response, "/cvError.jsp");
        }
    }

    /**
     * Helper: sends a single error message to cvError.jsp.
     */
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add(message);
        request.setAttribute("errorMessage",
                "Please correct the following problem(s) and submit again:");
        request.setAttribute("errorList", errors);
        forward(request, response, "/cvError.jsp");
    }

    /**
     * Helper: forwards the request and response to a JSP using RequestDispatcher.
     */
    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }

    /**
     * Builds a CVBean pre-populated with realistic dummy data using setters.
     * All details below are fictional.
     */
    private CVBean createSampleBean() {
        CVBean b = new CVBean();

        // Personal info
        b.setFullName("Ahmad Danial Bin Ismail");
        b.setProfessionalTitle("Junior Software Engineer");
        b.setEmail("ahmad.danial@example.com");
        b.setPhone("+60 12-345 6789");
        b.setProfileUrl("https://www.linkedin.com/in/ahmad-danial");
        b.setProfileSummary("Motivated computer science graduate with hands-on experience in "
                + "building Java EE web applications. Passionate about clean code, "
                + "MVC architecture and continuous learning.");

        // Sample photo: a simple avatar drawn as SVG (no image file needed)
        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='200' height='200' viewBox='0 0 200 200'>"
                + "<rect width='200' height='200' fill='#dfe3ec'/>"
                + "<circle cx='100' cy='78' r='38' fill='#2c3e8f'/>"
                + "<path d='M30 200c0-45 31-72 70-72s70 27 70 72z' fill='#2c3e8f'/>"
                + "</svg>";
        b.setPhotoData(svg.getBytes(StandardCharsets.UTF_8), "image/svg+xml");

        // Education
        b.setHighestDegree("Bachelor of Computer Science (Hons.)");
        b.setInstitution("Universiti Teknologi MARA, Kuala Terengganu");
        b.setGraduationYear("2024");
        b.setMajor("Software Engineering");

        // Experience
        b.setJobTitle("Software Developer Intern");
        b.setCompany("TechNusa Solutions Sdn. Bhd.");
        b.setDuration("Jun 2023 - Dec 2023");
        b.setResponsibilities(
                "Developed and maintained internal web modules using Java Servlet and JSP\n"
                + "Designed MySQL database tables and optimised slow queries by 30%\n"
                + "Collaborated with a team of 5 developers using Git and Agile Scrum\n"
                + "Wrote unit tests that increased code coverage to 80%");

        // Skills
        b.setTechnicalSkills("Java, JSP/Servlet, MySQL, HTML5, CSS3, JavaScript, Git, Apache Tomcat");
        b.setSoftSkills("Teamwork, Communication, Problem Solving, Time Management");

        return b;
    }
}