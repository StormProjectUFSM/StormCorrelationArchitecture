#!/bin/bash

cd ..
sudo git add . -A
sudo git commit -m "$1"
sudo git push
