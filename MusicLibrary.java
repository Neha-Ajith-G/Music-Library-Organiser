import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MusicLibrary extends JFrame {

    private JPanel p;
    private JPanel labelPanel;
    private JPanel buttonPanel;
    private JLabel imageLabel;

    private JLabel l1;
    private JButton b1, b2, b3, b4, b5, b6, b7, b8, b9;

    private Connection connection;

    public MusicLibrary() {
        super("Music Library");

        connectToDatabase();
        // Set the frame background color to black
        getContentPane().setBackground(Color.BLACK);

        p = new JPanel(new BorderLayout()); // Outer panel 
        p.setBackground(Color.BLACK);

        labelPanel = new JPanel();
        l1 = new JLabel("Music Library Organizer");
        l1.setForeground(Color.WHITE); // Set label text color to white
        labelPanel.setBackground(Color.BLACK); // Set label panel background color to black

        // Add the image below the label
        // Add the image below the label
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\gulab\\Documents\\MusicLibraryManager\\BGFile.jpeg");
        Image image = imageIcon.getImage(); // transform 
        Image newImg = image.getScaledInstance(300, 200, Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newImg); // transform it back
        imageLabel = new JLabel(imageIcon);
        labelPanel.add(imageLabel);

        

        buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10)); // Inner panel with GridLayout (3 rows, 3 columns) and spacing
        buttonPanel.setBackground(Color.BLACK); // Set button panel background color to black


        b1 = new JButton("Add Artist");
        b2 = new JButton("Add Album");
        b3 = new JButton("Add Song");
        b4 = new JButton("Display Artists");
        b5 = new JButton("Display Albums");
        b6 = new JButton("Display Songs");
        b7 = new JButton("Delete Artist");
        b8 = new JButton("Delete Album");
        b9 = new JButton("Delete Song");

        // Set button text color to white
        b1.setForeground(Color.WHITE);
        b2.setForeground(Color.WHITE);
        b3.setForeground(Color.WHITE);
        b4.setForeground(Color.WHITE);
        b5.setForeground(Color.WHITE);
        b6.setForeground(Color.WHITE);
        b7.setForeground(Color.WHITE);
        b8.setForeground(Color.WHITE);
        b9.setForeground(Color.WHITE);

        // Set button background color to black
        b1.setBackground(Color.BLACK);
        b2.setBackground(Color.BLACK);
        b3.setBackground(Color.BLACK);
        b4.setBackground(Color.BLACK);
        b5.setBackground(Color.BLACK);
        b6.setBackground(Color.BLACK);
        b7.setBackground(Color.BLACK);
        b8.setBackground(Color.BLACK);
        b9.setBackground(Color.BLACK);

        // Add action listeners to the buttons
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddArtistDialog();
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddAlbumDialog();
            }
        });
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddSongDialog();
            }
        });
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArtists();
            }
        });
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAlbums();
            }
        });
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySongs();
            }
        });

        b7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteArtistDialog();
            }
        });

        b8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteAlbumDialog();
            }
        });

        b9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteSongDialog();
            }
        });

        // Add buttons directly to the buttonPanel with FlowLayout for each button
        buttonPanel.add(createButtonPanel(b1));
        buttonPanel.add(createButtonPanel(b2));
        buttonPanel.add(createButtonPanel(b3));
        buttonPanel.add(createButtonPanel(b4));
        buttonPanel.add(createButtonPanel(b5));
        buttonPanel.add(createButtonPanel(b6));
        buttonPanel.add(createButtonPanel(b7));
        buttonPanel.add(createButtonPanel(b8));
        buttonPanel.add(createButtonPanel(b9));

        p.add(labelPanel, BorderLayout.NORTH);
        p.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(p); // Set the main panel as the content pane
        setSize(500, 400); // Adjusted size for better visibility of the layout
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Connect to the MySQL database
        connectToDatabase();
    }

    private JPanel createButtonPanel(JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center the button inside the panel
        panel.add(button);
        panel.setBackground(Color.BLACK); // Set button panel background color to black
        return panel;
    }

    private void connectToDatabase() {
        try {
            // Load the JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to your MySQL database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicLibrary", "root", "r4r84848");
            System.out.println("Connected to database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void openAddArtistDialog() {
        JFrame dialog = new JFrame("Add Artist");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        panel.add(new JLabel("Artist ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                addArtistToDatabase(id, name); // Add artist to the database
                dialog.dispose(); // Close the dialog
            }
        });
        panel.add(addButton);
        dialog.add(panel);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the dialog on close
        dialog.setVisible(true);
    }

    private void addArtistToDatabase(String id, String name) {
        try {
            String sql = "INSERT INTO Artist (Artist_ID, Artist_Name) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.executeUpdate();
            System.out.println("Artist added to database.");
        } catch (SQLException e) {
            System.err.println("Error adding artist to database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error adding artist: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddAlbumDialog() {
        JFrame dialog = new JFrame("Add Album");
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField yearField = new JTextField();
        panel.add(new JLabel("Album ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                String year = yearField.getText();
                addAlbumToDatabase(id, name, year); // Add album to the database
                dialog.dispose(); // Close the dialog
            }
        });
        panel.add(addButton);
        dialog.add(panel);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the dialog on close
        dialog.setVisible(true);
    }

    private void addAlbumToDatabase(String id, String name, String Year) {
        try {
            String sql = "INSERT INTO Album (Album_ID, Album_Name, Year) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, Year);
            statement.executeUpdate();
            System.out.println("Album added to database.");
        } catch (SQLException e) {
            System.err.println("Error adding album to database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error adding album: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddSongDialog() {
        JFrame dialog = new JFrame("Add Song");
        JPanel panel = new JPanel(new GridLayout(4, 3));
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField AlbField = new JTextField();
        JTextField ArtField = new JTextField();
        JTextField GenreField = new JTextField();
        JTextField DurationField = new JTextField();

        panel.add(new JLabel("Song ID:"));
        panel.add(idField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Genre:"));
        panel.add(GenreField);
        panel.add(new JLabel("Duration:"));
        panel.add(DurationField);
        panel.add(new JLabel("Album ID:"));
        panel.add(AlbField);
        panel.add(new JLabel("Artist ID:"));
        panel.add(ArtField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String title = titleField.getText();
                String alb = AlbField.getText();
                String art = ArtField.getText();
                String gen = GenreField.getText();
                String dur = DurationField.getText();

                addSongToDatabase(id, title, alb, art, gen, dur); // Add song to the database
                dialog.dispose(); // Close the dialog
            }
        });
        panel.add(addButton);
        dialog.add(panel);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the dialog on close
        dialog.setVisible(true);
    }

    private void addSongToDatabase(String id, String title, String AlbID, String ArtID, String Genre, String Duration) {
        try {
            String sql1 = "INSERT INTO Song (Song_ID, Song_Name, Album_ID, Genre, Duration) VALUES (?, ?, ?, ?, ?)";
            String sql2 = "INSERT INTO AlbArt_ref (Artist_ID, Album_ID, Song_ID) VALUES (?, ?, ?)";

            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, id);
            statement1.setString(2, title);
            statement1.setString(3, AlbID);
            statement1.setString(4, Genre);
            statement1.setString(5, Duration);
            statement1.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, ArtID);
            statement2.setString(2, AlbID);
            statement2.setString(3, id);
            statement2.executeUpdate();

            showInfoMess("Song added");
            System.out.println("Song added to database.");
        } catch (SQLException e) {
            System.err.println("Error adding song to database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error adding song: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayArtists() {
        JFrame displayFrame = new JFrame("Artists");
        displayFrame.setLayout(new GridLayout(0, 1));
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Artist");
    
            while (resultSet.next()) { // Move resultSet cursor to the next row
                String aName = resultSet.getString("Artist_Name");
    
                ResultSet resultSet1 = statement.executeQuery("SELECT s.Song_ID, s.Song_name, a.Album_Name, s.Genre, s.Duration FROM Song s INNER JOIN Album a ON s.Album_ID = a.Album_ID INNER JOIN AlbArt_ref aa ON s.Song_ID = aa.Song_ID INNER JOIN Artist ar ON aa.Artist_ID = ar.Artist_ID WHERE ar.Artist_Name = '" + aName + "'");
    
                while (resultSet1.next()) { // Move resultSet1 cursor to the next row
                    String artistName = resultSet.getString("Artist_Name");  // Should be resultSet1.getString()
                    String songName = resultSet1.getString("s.Song_Name");
                    String songAlbum = resultSet1.getString("a.Album_Name");
                    String songGenre = resultSet1.getString("s.Genre");
                    String songDuration = resultSet1.getString("s.Duration");
    
                    JButton artistButton = new JButton(artistName);  // Should be songName
                    artistButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(null, "Song: " + songName + " Album: " + songAlbum + " Genre: " + songGenre + " Duration: " + songDuration, "Artist Details", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                    displayFrame.add(artistButton);
                }
                resultSet1.close(); // Close the inner resultSet1 after using it
            }
            resultSet.close(); // Close the outer resultSet after using it
        } catch (SQLException e) {
            System.err.println("Error displaying artists: " + e.getMessage());
        }
        displayFrame.setSize(300, 400);
        displayFrame.setLocationRelativeTo(null);
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        displayFrame.setVisible(true);
    }    
    

    private void displayAlbums() {
        JFrame displayFrame = new JFrame("Albums");
        displayFrame.setLayout(new GridLayout(0, 1));
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Album");
            while (resultSet.next()) {
                String albumName = resultSet.getString("Album_Name");
                JButton albumButton = new JButton(albumName);
                albumButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "Album: " + albumName, "Album Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                displayFrame.add(albumButton);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Error displaying albums: " + e.getMessage());
        }
        displayFrame.setSize(300, 400);
        displayFrame.setLocationRelativeTo(null);
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        displayFrame.setVisible(true);
    }

    private void displaySongs() {
        JFrame displayFrame = new JFrame("Songs");
        displayFrame.setLayout(new GridLayout(0, 1));
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Song");
            while (resultSet.next()) {
                String songTitle = resultSet.getString("Song_Name");
                JButton songButton = new JButton(songTitle);
                songButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "Song: " + songTitle, "Song Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                displayFrame.add(songButton);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Error displaying songs: " + e.getMessage());
        }
        displayFrame.setSize(300, 400);
        displayFrame.setLocationRelativeTo(null);
        displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        displayFrame.setVisible(true);
    }

    private void openDeleteArtistDialog() {
        JFrame deleteFrame = new JFrame("Delete Artist");
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10)); // 3x3 grid layout with spacing
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        JTextField idField = new JTextField();
        panel.add(new JLabel("Artist ID:"));
        panel.add(idField);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                deleteArtistFromDatabase(id); // Delete artist from the database
                deleteFrame.dispose(); // Close the dialog
            }
        });
        panel.add(deleteButton);
        deleteFrame.add(panel);
        deleteFrame.setSize(300, 150);
        deleteFrame.setLocationRelativeTo(null);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the dialog on close
        deleteFrame.setVisible(true);
    }

    private void deleteArtistFromDatabase(String id) {
        try {
            String sql = "DELETE FROM Artist WHERE Artist_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.executeUpdate();
            System.out.println("Artist deleted from database.");
        } catch (SQLException e) {
            System.err.println("Error deleting artist from database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error deleting artist: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDeleteAlbumDialog() {
        JFrame deleteFrame = new JFrame("Delete Album");
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10)); // 3x3 grid layout with spacing
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        JTextField idField = new JTextField();
        panel.add(new JLabel("Album ID:"));
        panel.add(idField);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                deleteAlbumFromDatabase(id); // Delete album from the database
                deleteFrame.dispose(); // Close the dialog
            }
        });
        panel.add(deleteButton);
        deleteFrame.add(panel);
        deleteFrame.setSize(300, 150);
        deleteFrame.setLocationRelativeTo(null);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the dialog on close
        deleteFrame.setVisible(true);
    }

    private void deleteAlbumFromDatabase(String id) {
        try {
            String sql = "DELETE FROM Album WHERE Album_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.executeUpdate();
            System.out.println("Album deleted from database.");
        } catch (SQLException e) {
            System.err.println("Error deleting album from database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error deleting album: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDeleteSongDialog() {
        JFrame deleteFrame = new JFrame("Delete Song");
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10)); // 3x3 grid layout with spacing
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        JTextField idField = new JTextField();
        panel.add(new JLabel("Song ID:"));
        panel.add(idField);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                deleteSongFromDatabase(id); // Delete song from the database
                deleteFrame.dispose(); // Close the dialog
            }
        });
        panel.add(deleteButton);
        deleteFrame.add(panel);
        deleteFrame.setSize(300, 150);
        deleteFrame.setLocationRelativeTo(null);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the dialog on close
        deleteFrame.setVisible(true);
    }

    private void deleteSongFromDatabase(String id) {
        try {
            String sql1="DELETE from AlbArt_ref where Song_ID=?";
            String sql = "DELETE FROM Song WHERE Song_ID = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement1.setString(1, id);
            statement1.executeUpdate();
            statement.setString(1, id);
            statement.executeUpdate();
            System.out.println("Song deleted from database.");
            showInfoMess("Song deleted");
        } catch (SQLException e) {
            System.err.println("Error deleting song from database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error deleting song: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInfoMess(String info)
    {
        JOptionPane.showMessageDialog(this, info, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicLibrary());
    }
}
