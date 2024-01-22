public class Main {
    public static void main(String[] args) {

        // Création du personnage joueur
        Character player = new Character("Survivor", 100, 100, 0, null, null, null);

        // Initialisation de quelques ressources
        Resource water = new Resource("Water", 10);
        Resource food = new Resource("Food", 5);

        // Initialisation de quelques outils
        Tool axe = new Tool("Axe", 50, 0);
        Tool fishingRod = new Tool("Fishing Rod", 30, 0);

        // Initialisation de quelques dangers
        Danger wildAnimal = new Danger("Wild Animal", null, 70, 0);
        Danger storm = new Danger("Storm", null, 50, 0);

        // Logique pour démarrer le jeu
        // Par exemple, afficher un message de bienvenue, les instructions du jeu, etc.
        System.out.println("Welcome to the Survival Game!");
        System.out.println("Try to survive as long as you can.");

        player.collectResource(food);
        player.toString();
    }
}
