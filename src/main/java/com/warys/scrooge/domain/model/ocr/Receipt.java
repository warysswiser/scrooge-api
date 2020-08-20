package com.warys.scrooge.domain.model.ocr;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Setter
public class Receipt implements Serializable {

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}(-|\\/)\\d{2}(-|\\/)\\d{2}");
    private static final Pattern HOUR_PATTERN = Pattern.compile("\\d{2}(:)\\d{2}");
    private static final Pattern TOTAL_PATTERN = Pattern.compile("TOTAL.*\\d+(.|,)\\d*");
    private static final Pattern ITEM_PATTERN = Pattern.compile(".*\\d+(.|,)\\d*(€).*");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("\\d{1,2}(,|\\.)\\d{1,2}( )?(€|$)");

    private LocalDateTime creationDate;
    private double total;
    private List<ReceiptItem> items;

    public static Receipt fromRawData(String rawData) {
        final Receipt receipt = new Receipt();
        AtomicReference<LocalDateTime> creationDate = new AtomicReference<>();
        AtomicReference<Double> total = new AtomicReference<>((double) 0);
        List<ReceiptItem> items = new ArrayList<>();

        final List<String> collect = rawData.lines().collect(Collectors.toList());
        AtomicReference<String> prev = new AtomicReference<>();

        collect.forEach(x -> {
            String date = null;
            String hour = "00:00";
            final Matcher dateMatcher = DATE_PATTERN.matcher(x);
            while (dateMatcher.find()) {
                date = dateMatcher.group();
            }

            final Matcher hourMatcher = HOUR_PATTERN.matcher(x);
            while (hourMatcher.find()) {
                hour = hourMatcher.group();
            }

            if (date != null) {
                String str = date + " " + hour;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
                creationDate.set(LocalDateTime.parse(str, formatter));
            }

            final Matcher totalMatcher = TOTAL_PATTERN.matcher(x);
            if (totalMatcher.find()) {
                final String[] split = totalMatcher.group().split(" ");
                total.set(Double.parseDouble(split[split.length - 1].replace(",", ".")));
                return;
            }

            final Matcher itemsMatcher = ITEM_PATTERN.matcher(x);
            while (itemsMatcher.find()) {
                final String group = itemsMatcher.group();
                final Matcher amountMatcher = AMOUNT_PATTERN.matcher(group);

                //amountMatcher.
                while (amountMatcher.find()) {
                    if (!x.contains(total + "")) {
                        final Matcher multiMatcher = Pattern.compile("^\\d{1,2}(x)").matcher(x);
                        String amount;
                        String label;
                        amount = amountMatcher.group();
                        label = group.replaceFirst(amount, "")
                                .replace(" 11", "")
                                .replace("«", "")
                                .replace("- ", "")
                                .replace("À ", "")
                                .replace(":", "")
                                .replace("[", "")
                                .replace("]", "")
                                .replace(";", "")
                                .replace("\\s+$", "")
                                .replace("^\\s+", "");
                        items.add(new ReceiptItem(label, Double.parseDouble(amount
                                .replace("€", "")
                                .replace(",", "."))));
                    }
                }
            }
            prev.set(x);
        });

        receipt.setCreationDate(creationDate.get());
        receipt.setItems(items);
        receipt.setTotal(total.get());
        return receipt;
    }

}
