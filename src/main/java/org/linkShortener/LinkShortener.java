package org.linkShortener;

import java.util.*;

public class LinkShortener {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 10;

    private final int capacity;

    // lista que armazena as URLs originais, encurtadas e seus registros  de data/hora
    private LinkedList<LinkInfo> list;



    // mapa que mapeia as URLs originais para as URLs encurtadas
    private HashMap<String, String> urlMap;
    private HashMap<String, LinkInfo> shortUrlMap;

    public LinkShortener(int capacity) {
        this.capacity = capacity;
        this.list = new LinkedList<>();
        this.urlMap = new HashMap<>();
        this.shortUrlMap = new HashMap<>();
    }

    public String shortenUrl(String url, boolean isLongTerm) {
        // verifica se já existe uma URL encurtada para essa URL original
        String shortUrl = urlMap.get(url);
        if (shortUrl != null) {
            return shortUrl;
        }

        // gera uma nova URL encurtada aleatória
        shortUrl = generateShortUrl();

        // cria um novo nó com a URL original, encurtada e seu registro data/hora
        LinkInfo node = new LinkInfo(url, shortUrl, System.currentTimeMillis(), isLongTerm);

        list.addLast(node);
        urlMap.put(url, shortUrl);
        shortUrlMap.put(shortUrl, node);

        System.out.println(shortUrlMap);

        // remove a URL mais antiga se a lista atingir sua capacidade máxima
        if (list.size() > capacity) {
            removeOldestUrl();
        }

        // retorna a URL encurtada recém-gerada
        return shortUrl;
    }

    public String redirect(String shortUrl) {
        // procura a URL original correspondente à URL encurtada no mapa e retorna null se não encontrá-la
        LinkInfo node = shortUrlMap.getOrDefault(shortUrl, null);
        if (node != null) {
            return node.getOriginalUrl();
        }
        throw new ShortUrlNotFoundException();
    }

    private String generateShortUrl() {
        // gera uma URL encurtada aleatória com base nos caracteres possíveis
        StringBuilder stringB = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            stringB.append(CHARACTERS.charAt(index));
        }
        return stringB.toString();
    }

    private void removeOldestUrl() {
        // encontra o nó mais antigo na lista ligada com base no registro de data/hora e remove-o
        LinkInfo oldestNode = list.getFirst();
        for (LinkInfo node : list) {
            if (node.getTimestamp() < oldestNode.getTimestamp()) {
                oldestNode = node;
            }
        }
        list.remove(oldestNode);
        urlMap.remove(oldestNode.getOriginalUrl());
    }

}
