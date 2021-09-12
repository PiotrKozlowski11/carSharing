package carsharing;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class used to display menu items and handle user inputs
 */
class Menu {

    /**
     * Map of menu options. Integer is used for ordering items and to accept user input.
     * String is used to display message to user.
     */
    private final Map<Integer, String> itemList = new LinkedHashMap<>();


    public Menu() {

    }


    /**
     * Method to add another option to menu.
     * @param integer It will be used to accept user input from console
     * @param string It will be used to display message which is preceded by Number
     */
    public void putItemList(Integer integer, String string) {
        itemList.put(integer, string);
    }

    /**
     * Method used to generate Menu options as a String
     * @return String which can be used later to print a message to user
     */
    private String generateText() {
        StringBuilder str = new StringBuilder();
        itemList.forEach((val, text) -> str.append(String.format("%d. %s%n", val, text)));

        return str.toString();
    }

    /**
     * Method used to handle display options and handle user input.
     * Displayed numbers and messages are taken from itemList parameter.
     * Invalid inputs such as strings and doubles are ignored.
     * @return Method returns number which was earlier put by 'putItemList'
     */
    public int getChoice() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print(generateText());
                int actionNumber = Integer.parseInt(scanner.nextLine());


                if (itemList.containsKey(actionNumber))
                    return actionNumber;
                else
                    System.out.println("Invalid menu choice, " +
                            "Please enter number between " +
                            itemList.keySet().stream().min(Comparator.naturalOrder()).orElse(-1)
                            + " and " + itemList.keySet().stream().max(Comparator.naturalOrder()).orElse(-1));
            } catch (NumberFormatException e) {
                System.out.println("Invalid format");
            }
        }
    }


}
