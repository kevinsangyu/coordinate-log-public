package xyz.mlhmz.mcserverinformation.coordinatelog.stores.objects;

import lombok.Value;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;

import java.util.Collections;
import java.util.List;

@Value(staticConstructor = "create")
public class Page<T> {
    List<T> entries;
    int pageNumber;
    int allPages;

    public static <T> Page<T> of(List<T> entries, int page, int size) {
        int fromIndex = (page - 1) * size;

        if (entries.size() <= fromIndex) {
            return Page.empty();
        }

        List<T> subList = entries.subList(fromIndex, Math.min(fromIndex + size, entries.size()));
        return Page.create(subList, page, calculateAllPages(entries.size(), size));
    }

    /**
     * Divide the two values and ceil the result in order to also count a not completely full page
     *
     * @param listSize Actual size of the list that is being paginated
     * @param pageSize The max size of a page
     * @return How many pages a list results in
     */
    private static int calculateAllPages(int listSize, int pageSize) {
        return (listSize + pageSize - 1) / pageSize;
    }

    public static <T> Page<T> empty() {
        return Page.create(Collections.emptyList(), 0, 0);
    }
}
