package com.online.radar.kaf;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class GetCityTariff implements IGetTariff {
    public String getPriceEconom(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi.yandex.ru/" + str + "/tariff/econom").get().build()).execute();
            String replace = ((Element) Jsoup.parse(execute.body().string()).select("section").get(2)).getElementsByClass("MainPrices__price").text().replace("₽", "");
            if (replace.contains(",")) {
                replace = replace.substring(0, replace.indexOf(","));
            }
            if (execute != null) {
                execute.close();
            }
            return replace;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public String getPriceComfort(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi.yandex.ru/" + str + "/tariff/business").get().build()).execute();
            String replace = ((Element) Jsoup.parse(execute.body().string()).select("section").get(2)).getElementsByClass("MainPrices__price").text().replace("₽", "");
            if (replace.contains(",")) {
                replace = replace.substring(0, replace.indexOf(","));
            }
            if (execute != null) {
                execute.close();
            }
            return replace;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public String getPriceChildren(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi.yandex.ru/" + str + "/tariff/child_tariff").get().build()).execute();
            String replace = ((Element) Jsoup.parse(execute.body().string()).select("section").get(2)).getElementsByClass("MainPrices__price").text().replace("₽", "");
            if (replace.contains(",")) {
                replace = replace.substring(0, replace.indexOf(","));
            }
            if (execute != null) {
                execute.close();
            }
            return replace;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public String getPriceComfortPlus(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi.yandex.ru/" + str + "/tariff/comfortplus").get().build()).execute();
            String replace = ((Element) Jsoup.parse(execute.body().string()).select("section").get(2)).getElementsByClass("MainPrices__price").text().replace("₽", "");
            if (replace.contains(",")) {
                replace = replace.substring(0, replace.indexOf(","));
            }
            if (execute != null) {
                execute.close();
            }
            return replace;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public String getPriceVip(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi.yandex.ru/" + str + "/tariff/vip").get().build()).execute();
            String replace = ((Element) Jsoup.parse(execute.body().string()).select("section").get(2)).getElementsByClass("MainPrices__price").text().replace("₽", "");
            if (replace.contains(",")) {
                replace = replace.substring(0, replace.indexOf(","));
            }
            if (execute != null) {
                execute.close();
            }
            return replace;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public String getPriceMinivan(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi.yandex.ru/" + str + "/tariff/minivan").get().build()).execute();
            String replace = ((Element) Jsoup.parse(execute.body().string()).select("section").get(2)).getElementsByClass("MainPrices__price").text().replace("₽", "");
            if (replace.contains(",")) {
                replace = replace.substring(0, replace.indexOf(","));
            }
            if (execute != null) {
                execute.close();
            }
            return replace;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public String getPriceExpress(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi.yandex.ru/" + str + "/tariff/express").get().build()).execute();
            String replace = ((Element) Jsoup.parse(execute.body().string()).select("section").get(2)).getElementsByClass("MainPrices__price").text().replace("₽", "");
            if (replace.contains(",")) {
                replace = replace.substring(0, replace.indexOf(","));
            }
            if (execute != null) {
                execute.close();
            }
            return replace;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }
}
