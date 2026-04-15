#include <stdio.h>
#include <math.h>

typedef struct {
    double F;
    double sum;
} Result;

double sum_down_rec(int i, int n, double f1, double current, double sum) {
    if (i == n) {
        return sum;
    }

    double next = current * f1 * (3.0 - 2.0 * i) / (2.0 * i);
    return sum_down_rec(i + 1, n, f1, next, sum + next);
}

double sum_down(int n, double x) {
    double f1 = x / pow(0.525 + 0.5 * x, 2.0) - 1.0;
    return sum_down_rec(1, n, f1, f1, f1);
}

Result sum_up_rec(int i, double f1) {
    Result res;

    if (i == 1) {
        res.F = f1;
        res.sum = f1;
        return res;
    }

    Result prev = sum_up_rec(i - 1, f1);

    res.F = prev.F * f1 * (3.0 - 2.0 * (i - 1)) / (2.0 * (i - 1));
    res.sum = prev.sum + res.F;

    return res;
}

double sum_up(int n, double x) {
    double f1 = x / pow(0.525 + 0.5 * x, 2.0) - 1.0;
    Result res = sum_up_rec(n, f1);
    return res.sum;
}

double sum_mixed_rec(int i, int n, double f1, double current) {
    if (i == n) {
        return current;
    }

    double next = current * f1 * (3.0 - 2.0 * i) / (2.0 * i);
    return current + sum_mixed_rec(i + 1, n, f1, next);
}

double sum_mixed(int n, double x) {
    double f1 = x / pow(0.525 + 0.5 * x, 2.0) - 1.0;
    return sum_mixed_rec(1, n, f1, f1);
}

double loop(double x, int n) {
    double F1 = x / pow(0.525 + 0.5 * x, 2.0) - 1.0;
    double F = F1;
    double result = F1;

    for (int i = 1; i < n; i++) {
        F = F * F1 * (3.0 - 2.0 * i) / (2.0 * i);
        result += F;
    }

    return result;
}

int main() {
    double x;
    int n;

    printf("Enter your x: ");
    scanf("%lf", &x);
    printf("Enter your n: ");
    scanf("%d", &n);

    double result1 = sum_down(n, x);
    printf("First result: %lf\n", result1);

    double result2 = sum_up(n, x);
    printf("Second result: %lf\n", result2);

    double result3 = sum_mixed(n, x);
    printf("Third result: %lf\n", result3);

    double loopResult = loop(x, n);
    printf("Loop function result: %lf\n", loopResult);

    printf("sqrt(x): %lf\n", sqrt(x));

    return 0;
}
