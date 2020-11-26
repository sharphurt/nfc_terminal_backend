package ru.catstack.nfc_terminal.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPage implements Pageable {
    private final int limit;
    private final int offset;
    private final Sort sort;

    public OffsetBasedPage(int offset, int limit, Sort sort) {
        this.sort = sort;
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public @NotNull Sort getSort() {
        return sort;
    }

    @Override
    public @NotNull Pageable next() {
        return new OffsetBasedPage(getPageSize(), (int) (getOffset() + getPageSize()), sort);
    }

    private Pageable previous() {
        return hasPrevious() ? new OffsetBasedPage(getPageSize(), (int) (getOffset() - getPageSize()), sort) : this;
    }

    @Override
    public @NotNull Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public @NotNull Pageable first() {
        return new OffsetBasedPage(getPageSize(), 0, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}