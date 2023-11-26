package PDS;

public class Additonal {
    public static void timer(){
        for (int i = 5; i > 0; i--) {
            System.out.print(i + " ");
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n\n\n\n\n ");
    }
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
//                System.out.print("\u000C");
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            // Handle exceptions if necessary
        }
    }



    public static void randomSpace(){
        for (int i=10; i>0; i--){
            System.out.print("\n");
        }
    }

}
