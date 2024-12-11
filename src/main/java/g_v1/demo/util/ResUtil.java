package g_v1.demo.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import g_v1.demo.dto.res.PageInfo;
import g_v1.demo.dto.res.PageRes;
import g_v1.demo.dto.res.Res;

public class ResUtil {
    public static <T> ResponseEntity<?> buildRes(HttpStatus status, String message, T data) {
        Res<T> body = Res.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    public static <T> ResponseEntity<?> buildPageRes(HttpStatus status, String message, Page<T> page) {
        PageInfo pageInfo = PageInfo.builder()
                .total(page.getTotalElements())
                .pages(page.getTotalPages())
                .page(page.getPageable().getPageNumber() + 1)
                .size(page.getSize())
                .build();
        PageRes<List<T>> res = PageRes.<List<T>>builder()
                .status(status.value())
                .message(message)
                .data(page.getContent())
                .pagination(pageInfo)
                .build();
        return ResponseEntity.status(status).body(res);
    }
}
