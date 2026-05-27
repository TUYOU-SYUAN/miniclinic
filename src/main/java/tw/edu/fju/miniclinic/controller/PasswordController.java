package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;

@Controller
public class PasswordController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/password")
    public String showChangePasswordForm(Model model) {
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new PasswordForm());
        }
        return "password";
    }

    @PostMapping("/password")
    public String changePassword(
            @Valid @ModelAttribute("passwordForm") PasswordForm form,
            BindingResult result,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        // 1. 檢查基本表單驗證 (NotBlank, Size)
        if (result.hasErrors()) {
            return "password";
        }

        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");
        Doctor doctor = doctorRepository.findById(loggedInDoctorId).orElse(null);

        // 這應該不會發生，因為有 LoginRequiredInterceptor 保護，但作為防禦性編程
        if (doctor == null) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("errorMessage", "登入狀態異常，請重新登入。");
            return "redirect:/login";
        }

        // 2. 驗證舊密碼
        if (!BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            result.addError(new FieldError("passwordForm", "oldPassword", "舊密碼不正確"));
            return "password";
        }

        // 3. 驗證新密碼與確認密碼是否一致
        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            result.addError(new FieldError("passwordForm", "confirmNewPassword", "新密碼與確認密碼不一致"));
            return "password";
        }

        // 4. 更新密碼
        String newHashedPassword = BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt());
        doctor.setPasswordHash(newHashedPassword);
        doctorRepository.save(doctor);

        redirectAttributes.addFlashAttribute("successMessage", "密碼已成功修改！");
        return "redirect:/dashboard"; // 修改成功後重導到 Dashboard
    }
}