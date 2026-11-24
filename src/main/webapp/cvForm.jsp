<%-- 
    Document   : cvForm
    Description: View (MVC) - input form for CV data entry
    Author     : LENOVO
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CV Builder - Enter Your Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-form">
<div class="form-container">
    <h1>Dynamic Online CV Builder</h1>
    <p class="subtitle">Fill in your details to generate a professional CV.
        Fields marked <span class="req">*</span> are required.</p>

    <%-- GET flow: triggers CVServlet doGet() with action=sample --%>
    <div class="sample-box">
        <span>Want to see how it looks first?</span>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/CVServlet?action=sample">Preview Sample CV</a>
    </div>

    <%-- POST flow: triggers CVServlet doPost(). enctype is REQUIRED for file upload --%>
    <form action="${pageContext.request.contextPath}/CVServlet" method="post"
          enctype="multipart/form-data">

        <fieldset>
            <legend>Personal Information</legend>

            <%-- Photo upload with live preview --%>
            <label for="photo">Profile Photo (JPG, PNG or GIF, max 2 MB)</label>
            <div class="photo-upload">
                <img id="photoPreview" class="photo-preview" alt="Photo preview"
                     src="data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='100' height='100'><rect width='100' height='100' fill='%23dfe3ec'/><circle cx='50' cy='39' r='19' fill='%23a9b2cc'/><path d='M15 100c0-23 16-36 35-36s35 13 35 36z' fill='%23a9b2cc'/></svg>">
                <input type="file" id="photo" name="photo"
                       accept="image/png, image/jpeg, image/gif">
            </div>
            <small id="photoMsg" class="photo-msg"></small>

            <label for="fullName">Full Name <span class="req">*</span></label>
            <input type="text" id="fullName" name="fullName" placeholder="e.g. Aisyah Binti Rahman">

            <label for="professionalTitle">Professional Title</label>
            <input type="text" id="professionalTitle" name="professionalTitle" placeholder="e.g. Software Engineer">

            <div class="row">
                <div>
                    <label for="email">Email <span class="req">*</span></label>
                    <input type="text" id="email" name="email" placeholder="name@example.com">
                </div>
                <div>
                    <label for="phone">Phone Number</label>
                    <input type="text" id="phone" name="phone" placeholder="+60 12-345 6789">
                </div>
            </div>

            <label for="profileUrl">LinkedIn / GitHub URL</label>
            <input type="text" id="profileUrl" name="profileUrl" placeholder="https://github.com/username">

            <label for="profileSummary">Profile Summary</label>
            <textarea id="profileSummary" name="profileSummary" rows="4"
                      placeholder="A short introduction about yourself"></textarea>
        </fieldset>

        <fieldset>
            <legend>Education</legend>
            <label for="highestDegree">Highest Degree <span class="req">*</span></label>
            <select id="highestDegree" name="highestDegree">
                <option value="">-- Select --</option>
                <option>SPM / Certificate</option>
                <option>Diploma</option>
                <option>Bachelor's Degree</option>
                <option>Master's Degree</option>
                <option>PhD</option>
            </select>

            <label for="institution">Institution Name</label>
            <input type="text" id="institution" name="institution">

            <div class="row">
                <div>
                    <label for="graduationYear">Year of Graduation</label>
                    <input type="text" id="graduationYear" name="graduationYear" maxlength="4" placeholder="2024">
                </div>
                <div>
                    <label for="major">Major / Field</label>
                    <input type="text" id="major" name="major">
                </div>
            </div>
        </fieldset>

        <fieldset>
            <legend>Work Experience (Most Recent)</legend>
            <div class="row">
                <div>
                    <label for="jobTitle">Job Title</label>
                    <input type="text" id="jobTitle" name="jobTitle">
                </div>
                <div>
                    <label for="company">Company Name</label>
                    <input type="text" id="company" name="company">
                </div>
            </div>

            <label for="duration">Duration</label>
            <input type="text" id="duration" name="duration" placeholder="e.g. Jan 2022 - Present">

            <label for="responsibilities">Key Responsibilities / Achievements (one per line)</label>
            <textarea id="responsibilities" name="responsibilities" rows="5"></textarea>
        </fieldset>

        <fieldset>
            <legend>Skills</legend>
            <label for="technicalSkills">Technical Skills (comma-separated) <span class="req">*</span></label>
            <input type="text" id="technicalSkills" name="technicalSkills" placeholder="Java, SQL, HTML, CSS">

            <label for="softSkills">Soft Skills (comma-separated)</label>
            <input type="text" id="softSkills" name="softSkills" placeholder="Teamwork, Communication">
        </fieldset>

        <div class="actions">
            <button type="submit" class="btn btn-primary">Generate My CV</button>
            <button type="reset" class="btn btn-secondary" id="resetBtn">Reset</button>
        </div>
    </form>
</div>

<%-- Presentation only: live photo preview + client-side size check --%>
<script>
    (function () {
        var input = document.getElementById('photo');
        var preview = document.getElementById('photoPreview');
        var msg = document.getElementById('photoMsg');
        var placeholder = preview.src;
        var MAX = 2 * 1024 * 1024;

        input.addEventListener('change', function () {
            msg.textContent = '';
            var file = input.files[0];
            if (!file) { preview.src = placeholder; return; }

            if (file.size > MAX) {
                msg.textContent = 'This photo is larger than 2 MB. Please choose a smaller one.';
                input.value = '';
                preview.src = placeholder;
                return;
            }
            preview.src = URL.createObjectURL(file);
        });

        document.getElementById('resetBtn').addEventListener('click', function () {
            preview.src = placeholder;
            msg.textContent = '';
        });
    })();
</script>
</body>
</html>