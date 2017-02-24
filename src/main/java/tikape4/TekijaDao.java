package tikape4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TekijaDao {

    private String tietokantaosoite;

    public TekijaDao(String tietokantaosoite) {
        this.tietokantaosoite = tietokantaosoite;
    }

    public void luoTekija(String nimi) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Tekija(nimi) "
                + "VALUES ( ? )");
        stmt.setString(1, nimi);
        stmt.execute();

        conn.close();

    }

    public Tekija haeYksi(int key) throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tekija WHERE id = ?");
        stmt.setObject(1, key);
        ResultSet result = stmt.executeQuery();
        int id = result.getInt("id");
        String nimi = result.getString("nimi");
        Tekija tyyppi = new Tekija(id, nimi);
        conn.close();
        result.close();
        return tyyppi;
    }

    public List<Tekija> haeTekijat() throws Exception {
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM Tekija");

        List<Tekija> tekijat = new ArrayList<>();

        while (result.next()) {
            String nimi = result.getString("nimi");
            int id = result.getInt("id");

            Tekija tekija = new Tekija(id, nimi);
            tekijat.add(tekija);
        }

        conn.close();

        return tekijat;
    }
    
    public void poistaTekija(int key) throws Exception{
        Connection conn = DriverManager.getConnection(tietokantaosoite);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Tekija WHERE id = ?");
        PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Todo WHERE tekija_id = ?");
        stmt.setInt(1, key);        
        stmt2.setInt(1, key);        
        stmt.execute();
        stmt2.execute();
        conn.close();

    }
    
}
