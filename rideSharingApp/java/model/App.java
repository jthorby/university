package model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.RSS;

import javafx.application.Application;

import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;

import java.io.*;


public class App extends Application {
    private static Gson gson = FxGson.coreBuilder().setPrettyPrinting().create();
    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("UC Ride Sharing");

        ViewNavigator.toLogin(stage); // Boot to login screen
//        ViewNavigator.toMaster(stage); // Boot to master screen
    }

    public static RSS load() throws UnsupportedEncodingException {
        Reader reader = new InputStreamReader(App.class.getResourceAsStream("/data.json"), "UTF-8");
        return gson.fromJson(reader, RSS.class);
    }

    public static void save(RSS system) {
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream("src/main/resources/data.json"), "UTF-8");
            gson.toJson(system, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving system");
        }
    }

    public static void main(String[] args) {
        try {
            RSS rss = load();
            rss.setInstance(rss);

            launch();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encountered error loading system");
            e.printStackTrace();
        }
    }
}
