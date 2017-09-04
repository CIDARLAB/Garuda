#!/bin/bash

python -W ignore resources/visual/plot_SBOL_designs.py -params resources/visual/plot_parameters.csv -parts resources/visual/generated/part_information-3.csv -designs resources/visual/generated/dna_designs-3.csv -regulation resources/visual/reg_information.csv -output src/main/resources/static/images/generated/3.png
