#include <stdio.h>
#include <math.h>

typedef struct
{
    double F;
    double S;
} Wrapper;

double rDescent(double x, int i, int n, double F, double result)
{
    result += F;

    if (n == 1)
    {
        return result;
    }
    else
    {
        double F1 = x / (pow((0.525 + 0.5 * x), 2) - 1);
        double FNext = F * F1 * (3.0 - 2.0 * i) / (2.0 * i);

        return rDescent(x, i + 1, n - 1, FNext, result);
    }
}

Wrapper rReturn(double x, int i, int n)
{
    Wrapper result;

    if (i == 1)
    {
        result.F = x / (pow((0.525 + 0.5 * x), 2) - 1);
        result.S = result.F;
        return result;
    }

    double F1 = x / (pow((0.525 + 0.5 * x), 2) - 1);
    Wrapper prior = rReturn(x, i - 1, n);
    result.F = prior.F * F1 * (3.0 - 2.0 * (i - 1)) / (2.0 * (i - 1));
    result.S = prior.S + result.F;

    return result;
}

double rMixed(double x, int i, int n, double F)
{
    if (i > n)
    {
        return 0;
    }
    double F1 = x / (pow((0.525 + 0.5 * x), 2) - 1);
    double FNext = F * F1 * (3.0 - 2.0 * i) / (2.0 * i);
    double result = rMixed(x, i + 1, n, FNext);

    return result + F;
}

double loop(double x, int n, double F)
{
    double result = F;

    for (int i = 1; i < n; i++)
    {
        double F1 = x / (pow((0.525 + 0.5 * x), 2) - 1);
        F = F * F1 * (3.0 - 2.0 * i) / (2.0 * i);
        result += F;
    }
    return result;
}

int main()
{
    double x = 0.8;
    int n = 5;
    int i = 1;
    double result = 0;

    printf("Enter your x: ");
    scanf("%lf", &x);
    printf("Enter your n: ");
    scanf("%d", &n);

    double F = x / (pow((0.525 + 0.5 * x), 2) - 1);

    double result1 = rDescent(x, i, n, F, result);
    printf("First result: %lf\n", result1);

    double result2 = rReturn(x, n, n).S;
    printf("Second result: %lf\n", result2);

    double result3 = rMixed(x, i, n, F);
    printf("Third result: %lf\n", result3);

    double loopResult = loop(x, n, F);
    printf("Loop function result: %lf\n", loopResult);

    return 0;
}
