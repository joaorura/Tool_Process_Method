from shutil import copy as copy_file
from os.path import join
from copy import deepcopy
from math import ceil
from tqdm import tqdm
from ..utils.files import create_dir
from ..utils.list import max_list_index


class CreateDatasetFromDict:
    def __init__(self, dict_process: dict, base_path: str, names: list, sizes: list):
        count = 0
        for i in sizes:
            count += i

        if int(count) != 1:
            raise Exception('Error in sizes list')
        elif len(sizes) != len(names):
            raise Exception('Error in size of lists.')

        self.dict_process = dict_process
        self.paths = []

        create_dir(base_path)

        for i in names:
            path = join(base_path, i)
            create_dir(path)
            self.paths.append(path)

        self.sizes = sizes

    def _get_sizes(self, size: int):
        sizes: list = deepcopy(self.sizes)

        for i in range(0, len(sizes)):
            sizes[i] = ceil(sizes[i] * size)

        the_index = max_list_index(sizes)

        while sum(sizes) > size:
            sizes[the_index] -= 1

        return sizes

    def case_one(self, source: list):
        for i in source:
            for j in self.paths:
                copy_file(i, j, follow_symlinks=True)

    def case_two(self, source: list):
        sizes: list = self._get_sizes(len(source))
        count: int = 0

        for i in range(0, len(self.paths)):
            for j in range(0, sizes[i]):
                copy_file(source[count], self.paths[i], follow_symlinks=True)
                count += 1

        for i in range(count, len(source)):
            aux: int = count % len(self.paths)
            copy_file(source[count], self.paths[aux], follow_symlinks=True)

    def process(self):
        for i in tqdm(self.dict_process):
            element: list = self.dict_process[i]
            size: int = len(element)

            if size == 0:
                return False
            elif size <= 2:
                self.case_one(element)
            else:
                self.case_two(element)

        return True
