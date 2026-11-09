package com.deyo.hypixel.commons.scoreboard;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Getter
public final class ScoreboardData {
    private static final Pattern COLOR_PATTERN = Pattern.compile("§[0-9a-fk-orA-FK-OR]");
    private final String title;
    private final List<Line> lines;

    private ScoreboardData(String title, List<Line> lines) {
        this.title = title;
        this.lines = List.copyOf(lines);
    }

    public void forEachLine(Consumer<Line> consumer) {
        lines.forEach(consumer);
    }

    public boolean differs(ScoreboardData other) {
        if (other == null) return true;
        if (!title.equals(other.title)) return true;
        if (lines.size() != other.lines.size()) return true;
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).equals(other.lines.get(i))) {
                return true;
            }
        }
        return false;
    }

    public record Line(String text, int score) {
        public Line(String text) {
            this(text, 0);
        }

        public String plainText() {
            return COLOR_PATTERN.matcher(text).replaceAll("");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ScoreboardData of(String title, List<String> lines) {
        Builder builder = builder().title(title);
        for (int i = 0; i < lines.size(); i++) {
            builder.line(lines.get(i), lines.size() - i);
        }
        return builder.build();
    }

    public static class Builder {
        private String title = "";
        private final List<Line> lines = new ArrayList<>();

        public Builder title(String title) {
            this.title = title != null ? title : "";
            return this;
        }

        public Builder line(String text, int score) {
            lines.add(new Line(text != null ? text : "", score));
            return this;
        }

        public Builder line(String text) {
            return line(text, 15 - lines.size());
        }

        public Builder clear() {
            lines.clear();
            return this;
        }

        public ScoreboardData build() {
            return new ScoreboardData(title, lines);
        }
    }
}
