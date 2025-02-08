def fibonach_rec(n: int, counter: int = 0):
    def iterations(n: int) -> int:
        nonlocal counter
        counter += 1

        if n == 0:
            return 0
    
        elif n == 1 or n == 2:
            return 1
    
        return iterations(n - 1) + iterations(n - 2)
    
    return iterations(n), counter


def fibonachi_iter(n: int):
    if n <= 0:
        return 0, 1
    elif n == 1:
        return 1, 1
    
    a, b = 0, 1
    for _ in range(2, n + 1):
        a, b = b, a + b

    return b, n - 1