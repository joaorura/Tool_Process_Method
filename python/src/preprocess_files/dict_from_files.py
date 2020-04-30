import os
from re import sub
from copy import deepcopy


class DictFromFiles:
    def __init__(self, path: str, end_with: str, filters: list):
        self.path: str = path
        self.end_with: str = end_with
        self.filters: list = filters

        self.the_dict: dict = None

    def get_dict(self):
        for root, dirs, files in os.walk(self.path):
            if self.the_dict is None:
                self.the_dict = {}
                for file in files:
                    if file.endswith(self.end_with):
                        class_name = deepcopy(file)

                        for the_filter in self.filters:
                            class_name = sub(the_filter, "", class_name)

                        class_name = class_name.replace(self.end_with, "").lower()

                        if class_name not in self.the_dict:
                            self.the_dict[class_name] = []

                        self.the_dict[class_name].append(os.path.join(root, file))

        return self.the_dict
