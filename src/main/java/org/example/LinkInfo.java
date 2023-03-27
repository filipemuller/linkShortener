package org.example;

// classe interna que encapsula a lógica de armazenamento de uma URL original, encurtada e seu resgitro de data/hora
class LinkInfo {
    private final String originalUrl;
    private final String shortUrl;
    private final long timestamp;

    public LinkInfo(String originalUrl, String shortUrl, long timestamp) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.timestamp = timestamp;
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
