public class Dog {
    String nickName;
    int theirAge;
    boolean favouriteBreed;

    public Dog(String name, int age, boolean isHusky) {
        nickName = name;
        theirAge = age;
        favouriteBreed = isHusky;      
    }

    public static void main(String[] args) {
        Dog wolfDog = new Dog("Rob", 5, false);
        System.out.println(wolfDog.theirAge);
        System.out.println(wolfDog.favouriteBreed);

        Dog trueHusky = new Dog("Sunny", 4, true);
        System.out.println(trueHusky.theirAge > wolfDog.theirAge);
        System.out.println("Are they a husky? Let's find out! The answer is ... " + trueHusky.favouriteBreed +"!");
        System.out.println("Who is a good doggy? That's right! Both " + wolfDog.nickName + " and " + trueHusky.nickName + " " + "are!");
    }
}    
