import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RubiksCubeGUI {

    private static final int TILE_SIZE = 50; // Size of each tile
    private static final int GRID_SIZE = 3; // 3x3 grid for each face
    private static final String[] COMMANDS = {
            "U+", "U-", "B+", "B-", "R+", "R-", "L+", "L-", "F+", "F-", "EX"
    };

    private static final String COMMAND_DESCRIPTIONS = 
    "<html>" +
    "<div style='text-align: center; font-size: 20px; font-weight: bold; margin-bottom: 20px;'>RUBIK'S CUBE GAME</div>" +
    "Commands:<br>" +
    "&nbsp;&nbsp;U+ - Top layer is rotated clockwise<br>" +
    "&nbsp;&nbsp;U- - Top layer is rotated counterclockwise<br>" +
    "&nbsp;&nbsp;B+ - Bottom layer is rotated clockwise<br>" +
    "&nbsp;&nbsp;B- - Bottom layer is rotated counterclockwise<br>" +
    "&nbsp;&nbsp;R+ - Right layer is rotated clockwise<br>" +
    "&nbsp;&nbsp;R- - Right layer is rotated counterclockwise<br>" +
    "&nbsp;&nbsp;L+ - Left layer is rotated clockwise<br>" +
    "&nbsp;&nbsp;L- - Left layer is rotated counterclockwise<br>" +
    "&nbsp;&nbsp;F+ - Front face is rotated clockwise<br>" +
    "&nbsp;&nbsp;F- - Front face is rotated counterclockwise<br>" +
    "&nbsp;&nbsp;EX - Exit</html>";

    // Cube faces represented as 2D arrays of Colors
    private Color[][][] cube = new Color[6][GRID_SIZE][GRID_SIZE];
    private JPanel mainPanel;

    public RubiksCubeGUI() {
        initializeCube(); // Initialize the cube with default colors
    }

    private void initializeCube() {
        // Define colors for each face: White, Red, Blue, Orange, Green, Yellow
        Color[] colors = {Color.WHITE, Color.RED, Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW};
        for (int face = 0; face < 6; face++) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    cube[face][i][j] = colors[face];
                }
            }
        }
    }

    private JPanel createFacePanel(Color[][] face) {
        JPanel facePanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JPanel tile = new JPanel();
                tile.setBackground(face[i][j]);
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
                facePanel.add(tile);
            }
        }
        return facePanel;
    }

    private void shuffleCube() {
        Random random = new Random();
        Color[] colors = {Color.WHITE, Color.RED, Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW};
        for (int face = 0; face < 6; face++) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    cube[face][i][j] = colors[random.nextInt(colors.length)];
                }
            }
        }
        updateCubeDisplay();
    }

    private void updateCubeDisplay() {
        mainPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1; gbc.gridy = 0; // Top face
        mainPanel.add(createFacePanel(cube[0]), gbc);

        gbc.gridx = 0; gbc.gridy = 1; // Left face
        mainPanel.add(createFacePanel(cube[1]), gbc);

        gbc.gridx = 1; gbc.gridy = 1; // Front face
        mainPanel.add(createFacePanel(cube[2]), gbc);

        gbc.gridx = 2; gbc.gridy = 1; // Right face
        mainPanel.add(createFacePanel(cube[3]), gbc);

        gbc.gridx = 1; gbc.gridy = 2; // Bottom face
        mainPanel.add(createFacePanel(cube[4]), gbc);

        gbc.gridx = 3; gbc.gridy = 1; // Back face
        mainPanel.add(createFacePanel(cube[5]), gbc);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void rotateLayer(String command) {
        switch (command) {
            case "U+":
                rotateFaceClockwise(0); // Top face
                rotateEdgesForUPlus();
                break;
            case "U-":
                rotateFaceCounterClockwise(0); // Top face
                rotateEdgesForUMinus();
                break;
            case "B+":
                rotateFaceClockwise(5); // Back face
                rotateEdgesForBPlus();
                break;
            case "B-":
                rotateFaceCounterClockwise(5); // Back face
                rotateEdgesForBMinus();
                break;
            case "R+":
                rotateFaceClockwise(3); // Right face
                rotateEdgesForRPlus();
                break;
            case "R-":
                rotateFaceCounterClockwise(3); // Right face
                rotateEdgesForRMinus();
                break;
            case "L+":
                rotateFaceClockwise(1); // Left face
                rotateEdgesForLPlus();
                break;
            case "L-":
                rotateFaceCounterClockwise(1); // Left face
                rotateEdgesForLMinus();
                break;
            case "F+":
                rotateFaceClockwise(2); // Front face
                rotateEdgesForFPlus();
                break;
            case "F-":
                rotateFaceCounterClockwise(2); // Front face
                rotateEdgesForFMinus();
                break;
            case "EX":
                System.exit(0);
                return;
            default:
                System.out.println("Unknown command: " + command);
        }
        updateCubeDisplay();
    }
    
    private void rotateFaceClockwise(int faceIndex) {
        Color[][] face = cube[faceIndex];
        Color[][] temp = new Color[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                temp[j][GRID_SIZE - 1 - i] = face[i][j];
            }
        }
        cube[faceIndex] = temp;
    }

    private void rotateFaceCounterClockwise(int faceIndex) {
        Color[][] face = cube[faceIndex];
        Color[][] temp = new Color[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                temp[GRID_SIZE - 1 - j][i] = face[i][j];
            }
        }
        cube[faceIndex] = temp;
    }

    private void rotateEdgesForUPlus() {
        Color[] temp = cube[1][0].clone(); // Save top row of left face
        cube[1][0] = cube[2][0]; // Front top row -> Left top row
        cube[2][0] = cube[3][0]; // Right top row -> Front top row
        cube[3][0] = cube[5][0]; // Back top row -> Right top row
        cube[5][0] = temp;       // Left top row -> Back top row
    }

    private void rotateEdgesForUMinus() {
        Color[] temp = cube[1][0].clone(); // Save top row of left face
        cube[1][0] = cube[5][0]; // Back top row -> Left top row
        cube[5][0] = cube[3][0]; // Right top row -> Back top row
        cube[3][0] = cube[2][0]; // Front top row -> Right top row
        cube[2][0] = temp;       // Left top row -> Front top row
    }

    private void rotateEdgesForBPlus() {
        Color[] temp = new Color[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) temp[i] = cube[1][i][0]; // Save the left face's left column
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[1][i][0] = cube[4][2][GRID_SIZE - 1 - i]; // Left face's left column -> Top face's top row
            cube[4][2][GRID_SIZE - 1 - i] = cube[3][i][2]; // Bottom face's bottom row -> Left face's left column
            cube[3][i][2] = cube[0][0][i]; // Right face's right column -> Bottom face's bottom row
            cube[0][0][i] = temp[i]; // Top face's top row -> Right face's right column
        }
        rotateFaceClockwise(5); // Rotate the back face clockwise
    }
    
    
    private void rotateEdgesForBMinus() {
        Color[] temp = new Color[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) temp[i] = cube[1][i][0]; // Save the left face's left column
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[1][i][0] = cube[0][0][i]; // Top face's top row -> Left face's left column
            cube[0][0][i] = cube[3][i][2]; // Right face's right column -> Top face's top row
            cube[3][i][2] = cube[4][2][GRID_SIZE - 1 - i]; // Bottom face's bottom row -> Right face's right column
            cube[4][2][GRID_SIZE - 1 - i] = temp[i]; // Left face's left column -> Bottom face's bottom row
        }
        rotateFaceCounterClockwise(5); // Rotate the back face counterclockwise
    }
        
    
    private void rotateEdgesForRPlus() {
        Color[] temp = new Color[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) temp[i] = cube[0][i][2]; // Save right column of top face
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[0][i][2] = cube[2][i][2]; // Front right column -> Top right column
            cube[2][i][2] = cube[4][i][2]; // Bottom right column -> Front right column
            cube[4][i][2] = cube[5][GRID_SIZE - 1 - i][0]; // Back left column -> Bottom right column
            cube[5][GRID_SIZE - 1 - i][0] = temp[i]; // Top right column -> Back left column
        }
    }
    
    private void rotateEdgesForRMinus() {
        Color[] temp = new Color[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) temp[i] = cube[0][i][2]; // Save right column of top face
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[0][i][2] = cube[5][GRID_SIZE - 1 - i][0]; // Back left column -> Top right column
            cube[5][GRID_SIZE - 1 - i][0] = cube[4][i][2]; // Bottom right column -> Back left column
            cube[4][i][2] = cube[2][i][2]; // Front right column -> Bottom right column
            cube[2][i][2] = temp[i]; // Top right column -> Front right column
        }
    }

    private void rotateEdgesForLPlus() {
        Color[] temp = new Color[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) temp[i] = cube[0][i][0]; // Save left column of top face
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[0][i][0] = cube[5][GRID_SIZE - 1 - i][2]; // Back right column -> Top left column
            cube[5][GRID_SIZE - 1 - i][2] = cube[4][i][0]; // Bottom left column -> Back right column
            cube[4][i][0] = cube[2][i][0]; // Front left column -> Bottom left column
            cube[2][i][0] = temp[i]; // Top left column -> Front left column
        }
    }
    
    private void rotateEdgesForLMinus() {
        Color[] temp = new Color[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) temp[i] = cube[0][i][0]; // Save left column of top face
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[0][i][0] = cube[2][i][0]; // Front left column -> Top left column
            cube[2][i][0] = cube[4][i][0]; // Bottom left column -> Front left column
            cube[4][i][0] = cube[5][GRID_SIZE - 1 - i][2]; // Back right column -> Bottom left column
            cube[5][GRID_SIZE - 1 - i][2] = temp[i]; // Top left column -> Back right column
        }
    }
    
    private void rotateEdgesForFPlus() {
        Color[] temp = cube[0][2].clone(); // Save top face bottom row
    
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[0][2][i] = cube[1][GRID_SIZE - 1 - i][2]; // Left face right column -> Top face bottom row
            cube[1][GRID_SIZE - 1 - i][2] = cube[4][0][GRID_SIZE - 1 - i]; // Bottom face top row -> Left face right column
            cube[4][0][GRID_SIZE - 1 - i] = cube[3][i][0]; // Right face left column -> Bottom face top row
            cube[3][i][0] = temp[i]; // Top face bottom row -> Right face left column
        }
    }
    
    private void rotateEdgesForFMinus() {
        Color[] temp = cube[0][2].clone(); // Save top face bottom row
    
        for (int i = 0; i < GRID_SIZE; i++) {
            cube[0][2][i] = cube[3][i][0]; // Right face left column -> Top face bottom row
            cube[3][i][0] = cube[4][0][GRID_SIZE - 1 - i]; // Bottom face top row -> Right face left column
            cube[4][0][GRID_SIZE - 1 - i] = cube[1][GRID_SIZE - 1 - i][2]; // Left face right column -> Bottom face top row
            cube[1][GRID_SIZE - 1 - i][2] = temp[i]; // Top face bottom row -> Left face right column
        }
    }    
    
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Rubik's Cube");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel to hold the cube faces
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        updateCubeDisplay();

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.addActionListener(e -> shuffleCube());
        buttonPanel.add(shuffleButton);

        for (String command : COMMANDS) {
            JButton commandButton = new JButton(command);
            commandButton.addActionListener(e -> rotateLayer(command));
            buttonPanel.add(commandButton);
        }

        // Command Menu Panel
        JLabel commandMenu = new JLabel(COMMAND_DESCRIPTIONS);
        commandMenu.setHorizontalAlignment(SwingConstants.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(commandMenu, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RubiksCubeGUI gui = new RubiksCubeGUI();
            gui.createAndShowGUI();
        });
    }
}
