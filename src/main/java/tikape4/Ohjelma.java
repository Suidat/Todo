package tikape4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.port;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Ohjelma {

    public static void main(String[] args) throws Exception {
        port(getHerokuAssignedPort());

        // Database ... 
        TodoDao todoDao = new TodoDao("jdbc:sqlite:todo.db");
        TekijaDao tekijaDao = new TekijaDao("jdbc:sqlite:todo.db");

        Spark.get("/", (req, res) -> {
            res.redirect("/tekijat");
            return "ok";
        });

        Spark.get("/tehtavat/poista/:id", (req, res) -> {
            int id = todoDao.haeTekija(Integer.parseInt(req.params(":id")));
            todoDao.poista(req.params(":id"));
            res.redirect("/tekijat/" + id);
            return "ok";
        });

        Spark.get("/tekijat/poista/:id", (req, res) -> {
            tekijaDao.poistaTekija(Integer.parseInt(req.params(":id")));
            res.redirect("/tekijat");
            return "ok";
        });

        Spark.get("/tehtavat", (req, res) -> {
            HashMap data = new HashMap<>();

            data.put("tehtavat", todoDao.haeTodot());

            return new ModelAndView(data, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("/tehtavat", (req, res) -> {
            todoDao.lisaa(req.queryParams("tehtava"));
            res.redirect("/");
            return "ok";
        });

        Spark.get("/tekijat", (req, res) -> {
            HashMap data = new HashMap<>();

            data.put("tekijat", tekijaDao.haeTekijat());

            return new ModelAndView(data, "tekijat");
        }, new ThymeleafTemplateEngine());

        Spark.get("/tekijat/:id", (req, res) -> {
            HashMap data = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            data.put("tekija", tekijaDao.haeYksi(id));
            data.put("tehtavat", todoDao.haeTodot(id));

            return new ModelAndView(data, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("/tekijat", (req, res) -> {
            tekijaDao.luoTekija(req.queryParams("nimi"));
            res.redirect("/");
            return "ok";
        });

        Spark.post("/tekijat/:id", (req, res) -> {
            todoDao.lisaa(req.params(":id"),
                    req.queryParams("tehtava"));

            res.redirect("/tekijat/" + req.params(":id"));
            return "ok";
        });
        
        Spark.get("/tehtavat/tehty/:id", (req, res) ->{
            todoDao.tehty(Integer.parseInt(req.params(":id")));
            res.redirect("/tekijat/"+todoDao.haeTekija(Integer.parseInt(req.params(":id"))));
            return "ok";
        });
        

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
