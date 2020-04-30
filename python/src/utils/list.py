from sys import maxsize


def max_list_index(the_list: list):
    the_max: int = -maxsize
    index: int = 0

    for i in range(0, len(the_list)):
        if the_list[i] > the_max:
            the_max = the_list[i]
            index = i

    return index
