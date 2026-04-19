public class Beast {
    public static void main(String... args) {
        Animal animal = new TimurShemsedinov();
        animal.move();

        Animal animal2 = new DogNechai();
        animal2.move();
    }

    abstract static class Animal {
        public abstract void move();
    }

    static class TimurShemsedinov extends Animal {
        @Override
        public void move() {
            System.out.println("Пан Тимур Шемсадінов рухається");
        }
    }

    static class DogNechai extends Animal {
        @Override
        public void move() {
            System.out.println("Пан собака Нечай рухається");
        }
    }
}
