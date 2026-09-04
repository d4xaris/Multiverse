import java.util.Scanner;

public class thought4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] sym = {"+", "-", "/", "*"};

        for (int i = 0; i < n; i++) {
            int a = sc.nextInt();
            boolean found = false;

            for (int x = 0; x < 4 && !found; x++) {
                for (int g = 0; g < 4 && !found; g++) {
                    for (int t = 0; t < 4 && !found; t++) {
                        int n1 = 4, n2 = 4, n3 = 4, n4 = 4;
                        int o1 = x, o2 = g, o3 = t;

                        for (int rep = 0; rep < 3; rep++) {
                            if (o1 == 2 || o1 == 3) {
                                if (o1 == 2) {
                                    n1 = n1 / n2;
                                } else {
                                    n1 = n1 * n2;
                                }
                                n2 = n3;
                                n3 = n4;
                                o1 = o2;
                                o2 = o3;
                                o3 = -1;
                            }
                        }

                        for (int rep = 0; rep < 2; rep++) {
                            if (o2 == 2 || o2 == 3) {
                                if (o2 == 2) {
                                    n2 = n2 / n3;
                                } else {
                                    n2 = n2 * n3;
                                }
                                n3 = n4;
                                o2 = o3;
                                o3 = -1;
                            }
                        }

                        if (o3 == 2 || o3 == 3) {
                            if (o3 == 2) {
                                n3 = n3 / n4;
                            } else {
                                n3 = n3 * n4;
                            }
                        }

                        int result = n1;
                        if (o1 == 0) {
                            result = result + n2;
                        } else if (o1 == 1) {
                            result = result - n2;
                        }

                        if (o2 == 0) {
                            result = result + n3;
                        } else if (o2 == 1) {
                            result = result - n3;
                        }

                        if (o3 == 0) {
                            result = result + n4;
                        } else if (o3 == 1) {
                            result = result - n4;
                        }

                        if (result == a) {
                            System.out.println("4 " + sym[x] + " 4 " + sym[g] + " 4 " + sym[t] + " 4 = " + a);
                            found = true;
                        }
                    }
                }
            }

            if (!found) {
                System.out.println("no solution");
            }
        }
    }
}