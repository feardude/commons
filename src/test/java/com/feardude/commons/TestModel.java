package com.feardude.commons;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class TestModel {
    private final int id;
    private final String name;
    private final List<LocalDate> dates;
}
