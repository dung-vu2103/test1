package com.ringme.cms.controller.sys;

import com.ringme.cms.dto.TimeZoneData;
import com.ringme.cms.repository.sys.UserRepository;
import com.ringme.cms.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClienTimeZoneController {
    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/client-time-zone")
    @Transactional
    public ResponseEntity<String> setClientTimeZone(@RequestBody TimeZoneData timeZoneData) {
        try {
            // Xử lý dữ liệu timeZoneData ở đây
            String clientTimeZone = timeZoneData.getTimeZone();
            // Lưu dữ liệu vào cơ sở dữ liệu hoặc thực hiện các tác vụ khác

            String userId = userService.checkId();

            long id = Long.parseLong(userId);
            userRepository.setTimeZoneLogin(clientTimeZone, id);
            return ResponseEntity.ok("Múi giờ đã được cập nhật thành công.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Lỗi: ID người dùng không hợp lệ.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: Đã xảy ra lỗi không mong muốn.");
        }
    }
}
