public class Magic {
	public static void main(String[] args) {
        int myNumber = 67;
        int magicNumber = ((((myNumber * myNumber) + myNumber) / myNumber) + 17 - myNumber) / 6;
        System.out.println(magicNumber);
	}
}