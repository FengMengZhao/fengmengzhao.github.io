#!/bin/bash

sudo cp _posts/2021-06-25-docker-handbook-2021.markdown weixin/

#sed -i 's/![](img/![](https:\/\/gitee.com\/fengmengzhao\/fengmengzhao.github.io\/raw\/master\/img/g' 
sudo sed -i 's/!\[\](\/img/!\[\](https:\/\/gitee.com\/fengmengzhao\/fengmengzhao.github.io\/raw\/master\/img/g' weixin/2021-06-25-docker-handbook-2021.markdown
