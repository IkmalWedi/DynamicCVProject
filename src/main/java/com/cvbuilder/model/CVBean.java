package com.cvbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Model (MVC): holds candidate data and validation logic.
 */
public class CVBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Photo rules
    public static final long MAX_PHOTO_BYTES = 2L * 1024 * 1024; // 2 MB
    private static final List<String> ALLOWED_PHOTO_TYPES =
            Arrays.asList("image/jpeg", "image/png", "image/gif");

    // ---- Personal info ----
    private String fullName;
    private String professionalTitle;
    private String email;
    private String phone;
    private String profileUrl;      // LinkedIn / GitHub
    private String profileSummary;

    // ---- Photo ----
    private String photoContentType; // e.g. image/png
    private long photoSize;          // size in bytes
    private String photoBase64;      // image encoded as Base64 text

    // ---- Education ----
    private String highestDegree;
    private String institution;
    private String graduationYear;
    private String major;

    // ---- Experience ----
    private String jobTitle;
    private String company;
    private String duration;
    private String responsibilities; // one item per line

    // ---- Skills ----
    private String technicalSkills;  // comma-separated
    private String softSkills;       // comma-separated

    // ---- Validation errors ----
    private List<String> errors = new ArrayList<>();

    // Zero-argument constructor
    public CVBean() {
    }

    // ================= VALIDATION =================
    /**
     * Validates mandatory fields: Name, Email, Degree, at least one Skill.
     * Also checks format of email, graduation year, profile URL and photo.
     * @return true if all rules pass; messages are stored in getErrors().
     */
    public boolean validate() {
        errors.clear();

        if (isBlank(fullName)) {
            errors.add("Full Name is required.");
        }

        if (isBlank(email)) {
            errors.add("Email is required.");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Email format is invalid (e.g. name@example.com).");
        }

        if (isBlank(highestDegree)) {
            errors.add("Highest Degree is required.");
        }

        if (getTechnicalSkillsList().isEmpty() && getSoftSkillsList().isEmpty()) {
            errors.add("At least one skill (technical or soft) is required.");
        }

        if (!isBlank(graduationYear) && !graduationYear.matches("^(19|20)\\d{2}$")) {
            errors.add("Year of Graduation must be a valid 4-digit year (e.g. 2024).");
        }

        if (!isBlank(profileUrl) && !profileUrl.matches("^https?://\\S+$")) {
            errors.add("LinkedIn/GitHub URL must start with http:// or https://");
        }

        // Photo is optional, but if provided it must be valid
        if (isPhotoPresent()) {
            if (photoContentType == null
                    || !ALLOWED_PHOTO_TYPES.contains(photoContentType.toLowerCase())) {
                errors.add("Photo must be a JPG, PNG or GIF image.");
            }
            if (photoSize > MAX_PHOTO_BYTES) {
                errors.add("Photo is too large. Maximum size is 2 MB.");
            }
        }

        return errors.isEmpty();
    }

    // ================= PHOTO HELPERS =================
    /**
     * Stores the uploaded image: converts the raw bytes to Base64 text.
     * Passing null or an empty array removes the photo.
     */
    public void setPhotoData(byte[] data, String contentType) {
        if (data == null || data.length == 0) {
            this.photoBase64 = null;
            this.photoContentType = null;
            this.photoSize = 0;
            return;
        }
        this.photoSize = data.length;
        this.photoContentType = contentType;
        this.photoBase64 = Base64.getEncoder().encodeToString(data);
    }

    /** True when a photo has been stored. */
    public boolean isPhotoPresent() {
        return photoBase64 != null && !photoBase64.isEmpty();
    }

    /** Ready-to-use value for <img src="...">, or empty text if no photo. */
    public String getPhotoDataUri() {
        if (!isPhotoPresent()) {
            return "";
        }
        return "data:" + photoContentType + ";base64," + photoBase64;
    }

    // ================= HELPERS =================
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private List<String> splitToList(String text, String regex) {
        List<String> list = new ArrayList<>();
        if (!isBlank(text)) {
            for (String item : text.split(regex)) {
                if (!item.trim().isEmpty()) {
                    list.add(item.trim());
                }
            }
        }
        return list;
    }

    // Read-only derived properties (used by the View with JSTL forEach)
    public List<String> getTechnicalSkillsList() {
        return splitToList(technicalSkills, ",");
    }

    public List<String> getSoftSkillsList() {
        return splitToList(softSkills, ",");
    }

    public List<String> getResponsibilitiesList() {
        return splitToList(responsibilities, "\\r?\\n");
    }

    // ================= GETTERS & SETTERS =================
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = trim(fullName); }

    public String getProfessionalTitle() { return professionalTitle; }
    public void setProfessionalTitle(String professionalTitle) { this.professionalTitle = trim(professionalTitle); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = trim(email); }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = trim(phone); }

    public String getProfileUrl() { return profileUrl; }
    public void setProfileUrl(String profileUrl) { this.profileUrl = trim(profileUrl); }

    public String getProfileSummary() { return profileSummary; }
    public void setProfileSummary(String profileSummary) { this.profileSummary = trim(profileSummary); }

    public String getPhotoContentType() { return photoContentType; }
    public void setPhotoContentType(String photoContentType) { this.photoContentType = photoContentType; }

    public long getPhotoSize() { return photoSize; }
    public void setPhotoSize(long photoSize) { this.photoSize = photoSize; }

    public String getPhotoBase64() { return photoBase64; }
    public void setPhotoBase64(String photoBase64) { this.photoBase64 = photoBase64; }

    public String getHighestDegree() { return highestDegree; }
    public void setHighestDegree(String highestDegree) { this.highestDegree = trim(highestDegree); }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = trim(institution); }

    public String getGraduationYear() { return graduationYear; }
    public void setGraduationYear(String graduationYear) { this.graduationYear = trim(graduationYear); }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = trim(major); }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = trim(jobTitle); }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = trim(company); }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = trim(duration); }

    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = trim(responsibilities); }

    public String getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(String technicalSkills) { this.technicalSkills = trim(technicalSkills); }

    public String getSoftSkills() { return softSkills; }
    public void setSoftSkills(String softSkills) { this.softSkills = trim(softSkills); }

    public List<String> getErrors() { return errors; }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }
}