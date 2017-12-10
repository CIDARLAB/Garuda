#!/bin/bash

python -W ignore resources/visual/plot_SBOL_designs.py -params resources/visual/plot_parameters.csv -parts resources/visual/generated/part_information-13.csv -designs resources/visual/generated/dna_designs-13.csv -regulation resources/visual/reg_information.csv -output src/main/resources/static/images/generated/13.png
