package io.github.sardul3.expense.application.dto;

import java.util.List;

/**
 * Paginated response for expense history.
 *
 * @param content       list of activities for the current page
 * @param totalElements total number of activities
 * @param totalPages    total pages
 * @param number        current page number (0-based)
 * @param size          page size
 */
public record ExpenseHistoryPageResponse(
        List<ActivityView> content,
        long totalElements,
        int totalPages,
        int number,
        int size
) {
}
