from src.preprocess_files.dict_from_files import DictFromFiles
from src.create_dataset_from_files.create_dataset_from_dict import CreateDatasetFromDict

PATH: str = "/media/joaorura/7016D4F016D4B7F4/Workspace/Data/theTest_incomplete"
END_WITH: str = ".java"
BASE_PATH: str = "/media/joaorura/7016D4F016D4B7F4/Workspace/Data/theTest_dataset"

FILTERS: list = ['[0-9|]*']
NAMES: list = ['train', 'val']
SIZES: list = [0.8, 0.2]


def main():
    dict_process = DictFromFiles(PATH, END_WITH, FILTERS).get_dict()
    test = CreateDatasetFromDict(dict_process, BASE_PATH, NAMES, SIZES).process()

    if not test:
        print("Error ao executar!")


if __name__ == '__main__':
    main()
