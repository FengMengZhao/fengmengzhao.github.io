#!/bin/bash

if [ -z "$1" ]; then
  echo "需要输入参数：bash shell_scripts/img_to_giteeURL.sh 2021-12-20-who-is-responsible-for-my-last-5years-hardworking-but-pay-less.markdown，这里参数是Markdown文章，前面不能加路径。"
  exit
fi

/bin/cp _posts/$1 weixin/

#sed -i 's/!\[\](\/img/!\[\](https:\/\/gitee.com\/fengmengzhao\/fengmengzhao.github.io\/raw\/master\/img/g' weixin/$1
sed -i 's/\/img\/posts\//https:\/\/gitee.com\/fengmengzhao\/fengmengzhao.github.io\/raw\/master\/img\/posts\//g' weixin/$1
