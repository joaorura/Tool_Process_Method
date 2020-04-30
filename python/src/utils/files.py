from os import mkdir


def create_dir(the_dir):
    try:
        mkdir(the_dir)
    except FileExistsError:
        pass