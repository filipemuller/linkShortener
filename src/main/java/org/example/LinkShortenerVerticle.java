package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class LinkShortenerVerticle extends AbstractVerticle {
    private LinkShortener linkShortener;

    @Override
    public void start() throws Exception {
        linkShortener = new LinkShortener(100);

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.post("/links").handler(this::handleShortenUrl);
        router.get("/:shortUrl").handler(this::handleRedirect);

        server.requestHandler(router).listen(8080);
    }

    private void handleShortenUrl(RoutingContext routingContext) {
        JsonObject jsonBody = routingContext.getBodyAsJson();
        String url = jsonBody.getString("url");
        boolean longTerm = jsonBody.getBoolean("longTerm", false);

        String shortUrl = linkShortener.shortenUrl(url);

        JsonObject jsonResponse = new JsonObject().put("key", shortUrl);

        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json").end(jsonResponse.encode());
    }

    private void handleRedirect(RoutingContext routingContext) {
        String shortUrl = routingContext.pathParam("shortUrl");
        String originalUrl = linkShortener.redirect(shortUrl);

        if (originalUrl != null) {
            routingContext.response().putHeader("location", originalUrl).setStatusCode(302).end();
        } else {
            routingContext.fail(new ShortUrlNotFoundException());
        }
    }
}
