package org.linkShortener;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Map<String, String> urlMap = new HashMap<>();

    public static Map<String, Boolean> urlLongTermMap = new HashMap<>();
    private static LinkShortener linkShortener = new LinkShortener(1000);

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        // cria um roteador para manipular as solicitações de entrada
        Router router = Router.router(vertx);

        // adiciona um manipulador de corpo para lidar com solicitações de entrada que possuem um corpo JSON
        router.route().handler(BodyHandler.create());

        // adiciona um manipulador para o endpoint POST /links
        router.post("/links").handler(Main::handleShortenUrl);

        // adiciona um manipulador para o endpoint GET /:shortUrl
        router.get("/:shortUrl").handler(Main::handleRedirect);

        // inicia o servidor na porta 8080
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    private static void handleShortenUrl(RoutingContext routingContext) {

        // obtém o objeto JSON a partir do corpo da solicitação
        JsonObject jsonBody = routingContext.getBodyAsJson();

        // extrai a URL e a flag de longa duração do objeto JSON
        String url = jsonBody.getString("url");
        boolean longTerm = jsonBody.getBoolean("longTerm");

        // encurta a URL usando o LinkShortener
        String shortUrl = linkShortener.shortenUrl(url, longTerm);

        // adiciona a URL encurtada ao mapa
        urlMap.put(shortUrl, url);
        urlLongTermMap.put(shortUrl, longTerm);

        // cria um objeto JSON com a chave do link encurtado
        JsonObject jsonResponse = new JsonObject().put("key", shortUrl);

        // envia a resposta com o objeto JSON
        routingContext.response().putHeader("content-type", "application/json").end(jsonResponse.encode());
    }


    private static void handleRedirect(RoutingContext routingContext) {

        // obtém a URL encurtada a partir do parâmetro de caminho
        String shortUrl = routingContext.request().getParam("shortUrl");

        // verifica se a URL encurtada está presente no mapa
        if (urlMap.containsKey(shortUrl)) {

            // recupera a URL original do mapa
            String originalUrl = urlMap.get(shortUrl);
            Boolean urlIsLongTerm = urlLongTermMap.get(shortUrl);

            // imprime as informações do link
            System.out.println("Link acessado: " + shortUrl);
            System.out.println("Redirecionando para: " + originalUrl);
            //System.out.println("URL de longa duração: " + urlIsLongTerm);
            // System.out.println("");

            // redireciona para a URL original
            routingContext.response().putHeader("Location", originalUrl).setStatusCode(302).end();

        } else {
            // se a URL encurtada não estiver presente no mapa, lança uma exceção
            throw new ShortUrlNotFoundException();
        }
    }


}
