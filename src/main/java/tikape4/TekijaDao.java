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
}
