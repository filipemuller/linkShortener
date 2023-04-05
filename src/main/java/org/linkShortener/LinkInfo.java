package org.linkShortener;

// classe interna que encapsula a lógica de armazenamento de uma URL original, encurtada e seu resgitro de data/hora
class LinkInfo {
    private final String originalUrl;
    private final String shortUrl;
    private final long timestamp;

    private boolean isLongTerm;

    public LinkInfo(String originalUrl, String shortUrl, long timestamp, boolean isLongTerm) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.timestamp = timestamp;
        this.isLongTerm = isLongTerm();
    }
    public boolean isLongTerm() {
        return isLongTerm;
    }
    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
