//使用递归的方式实现数组、对象的深拷贝
export function deepClone (obj) {
    let objClone = Array.isArray(obj) ? [] : {};
    if (obj && typeof obj === "object") {
        for (let key in obj) {
            if (obj.hasOwnProperty(key)) {
                //判断ojb子元素是否为对象，如果是，递归复制
                if (obj[key] && typeof obj[key] === "object") {
                    objClone[key] = deepClone(obj[key])
                } else {
                    //如果不是，简单复制
                    objClone[key] = obj[key]
                }
            }
        }
    }
    return objClone
}