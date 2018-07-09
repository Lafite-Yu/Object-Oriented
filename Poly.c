// version 1.0

#include <stdio.h>

#define LENGTH 1000050

int TargetPoly[LENGTH];
int SourcePoly[LENGTH];

int main()
{
    int c = '+';
    while(c != '\n')
    {
        createPoly();
        computePoly(c);
        c = getchar();
    }
    printPoly();
}

void createPoly()
{
    for (int i = 0; i < LENGTH; ++i)
        SourcePoly[i] = 0;

    char num[7];
    int ch = 0;
    int c = 0, n = 0, i = 0;
    ch = getchar();
    while (ch != '}')
    {
        ch = getchar();
        i = 0;
        for (int j = 0; j < 7; ++j)
            num[j] = 0;
        ch = getchar();
        while(ch != ',')
        {
            num[i] = ch;
            i += 1;
            ch = getchar();
        }
        c = atoi(num);

        i = 0;
        for (int j = 0; j < 7; ++j)
            num[j] = 0;
        ch = getchar();
        while(ch != ')')
        {
            num[i] = ch;
            i += 1;
            ch = getchar();
        }
        n = atoi(num);

        SourcePoly[n] = c;
        ch = getchar();
    }
}

void computePoly(int op)
{
    if (op == '+')
    {
        for (int i = 0; i < LENGTH; ++i)
        {
            TargetPoly[i] += SourcePoly[i];
        }
    }
    else if (op == '-')
    {
        for (int i = 0; i < LENGTH; ++i)
        {
            TargetPoly[i] -= SourcePoly[i];
        }
    }
}

void printPoly()
{
    int flag = 0;
    for (int i = 0; i < LENGTH; i++)
    {
        if (TargetPoly[i] != 0)
        {
            if (flag == 1)
                printf(",");
            else
                printf("{");
            printf("(%d,%d)" ,TargetPoly[i], i);
            flag = 1;
        }
    }
    if (flag == 0)
        printf("0");
    else
        printf("}");
}
