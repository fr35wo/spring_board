package com.example.soulware_week1.board.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageRequestDto(
        @NotNull(message = "페이지 번호는 필수입니다.")
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        Integer page,

        @NotNull(message = "페이지 크기는 필수입니다.")
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        @Max(value = 10, message = "페이지 크기는 최대 10까지 가능합니다.")
        Integer size
) {
    public Pageable toPageable() {
        return PageRequest.of(page - 1, size, Sort.by("createAt").descending());
    }
}
