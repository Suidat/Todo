package tikape4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TodoDao {

    private String tietokantaosoite;

    public TodoDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public List<Todo> haeTodot() throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM Todo");

        List<Todo> tehtavat = new ArrayList<>();

        while (result.next()) {
            String task = result.getString("task");
            int id = result.getInt("id");
            boolean done = result.getBoolean("done");

            Todo todo = new Todo(id, task, done);
            tehtavat.add(todo);
        }

        conn.close();

        return tehtavat;
    }

    public List<Todo> haeTodot(int tekijanId) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Todo WHERE tekija_id = ?");
        stmt.setInt(1, tekijanId);
        ResultSet result = stmt.executeQuery();

        List<Todo> tehtavat = new ArrayList<>();

        while (result.next()) {
            String task = result.getString("task");
            int id = result.getInt("id");
            boolean done = result.getBoolean("done");

            Todo todo = new Todo(id, task, done);
            tehtavat.add(todo);
        }

        conn.close();

        return tehtavat;
    }

    public int haeTekija(int key) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Todo WHERE id = ?");
        stmt.setInt(1, key);
        ResultSet result = stmt.executeQuery();
        int id = result.getInt("tekija_id");

        conn.close();
        return id;
    }

    public void lisaa(String tehtava) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Todo (task, done) "
                + "VALUES (?, 0)");
        stmt.setString(1, tehtava);
        stmt.execute();

        conn.close();

    }

    public void poista(String id) throws Exception {
        try {
            Integer.parseInt(id);
        } catch (Throwable t) {
            return;
        }
        // 1%20OR%201=1
        // 1 OR 1=1
        // DELETE FROM Todo WHERE id = 1 OR 1=1
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Todo WHERE id = ?");
        stmt.setInt(1, Integer.parseInt(id));
        stmt.execute();
        conn.close();
    }

    public void lisaa(String id, String tehtava) throws Exception {
        try {
            Integer.parseInt(id);
        } catch (Throwable t) {
            return;
        }
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Todo (task, done, tekija_id) "
                + "VALUES (?, 0, ?)");
        stmt.setString(1, tehtava);
        stmt.setInt(2, Integer.parseInt(id));
        stmt.execute();

        conn.close();

    }

    public void tehty(int key) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("UPDATE Todo SET done = 1 WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        conn.close();
    }
}
