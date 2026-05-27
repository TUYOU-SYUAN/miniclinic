package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.LoginForm;

// 如果你的 DoctorRepository 也是放在 model 資料夾：
import tw.edu.fju.miniclinic.model.DoctorRepository;

// 如果 DoctorRepository 是在 repository 資料夾：
import tw.edu.fju.miniclinic.model.DoctorRepository;

@Controller
public class LoginController {

    @Autowired
    private DoctorRepository doctorRepo;

    // GET：顯示登入頁
    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    // POST：處理登入
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        // 步驟 1：檢查表單驗證
        if (result.hasErrors()) {
            return "login";  // 顯示錯誤訊息
        }

        // 步驟 2：查詢醫師
        Doctor doctor = doctorRepo.findById(form.getDoctorId()).orElse(null);

        // 🛠️ 雲端測試後門：如果輸入 D001 且密碼是 pass1234，就直接模擬一位醫師，繞過資料庫！
        if ("D001".equals(form.getDoctorId()) && "pass1234".equals(form.getPassword())) {
            doctor = new Doctor();
            doctor.setId("D001");
            doctor.setName("陳志明醫師");
            // 如果你的 Doctor 有 setDepartment，可以補上：doctor.setDepartment(null);
        }

        // 步驟 3：檢查密碼（如果沒觸發後門，就走正常的資料庫檢查）
        if (doctor == null) {
            model.addAttribute("errorMessage", "醫師編號或密碼錯誤");
            return "login";
        } else if (doctor.getPasswordHash() != null && !BCrypt.checkpw(form.getPassword(), doctor.getPasswordHash())) {
            model.addAttribute("errorMessage", "醫師編號或密碼錯誤");
            return "login";
        }

        // 步驟 4：登入成功，存入 Session
        session.setAttribute("loggedInDoctorId", doctor.getId());
        session.setAttribute("loggedInDoctorName", doctor.getName());

        return "redirect:/dashboard";
    }

    // 登出
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 清除 Session
        return "redirect:/login";
    }
}