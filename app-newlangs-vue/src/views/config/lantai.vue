<template>
    <div>
        <p>Thick Wave donates 10% of its monthly income to Blue State Public Welfare for public welfare activities.</p>
    </div>
    <center>
        <div class="card">
            <div class="card-content">
                <h1 class="card-title">Blue State Public Welfare</h1>
                <h1 class="card-para">For the Benefit of Heaven and Earth<br>For the Livelihood of the People<br>For the
                    Continuation of Wisdom from the Past<br>For the Eternal Peace of All Generations</h1>
                <a href="http://www.ltxfwh.org" target="_blank">
                    <button class="button">
                        <svg stroke="#ffffff" xml:space="preserve" viewBox="0 0 80 80"
                            xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg" id="Capa_1"
                            version="1.1" fill="#ffffff">
                            <g stroke-width="0" id="SVGRepo_bgCarrier"></g>
                            <g stroke-linejoin="round" stroke-linecap="round" id="SVGRepo_tracerCarrier"></g>
                            <g id="SVGRepo_iconCarrier">
                                <g>
                                    <path
                                        d="M64,48L64,48h-8V32h8c8.836,0,16-7.164,16-16S72.836,0,64,0c-8.837,0-16,7.164-16,16v8H32v-8c0-8.836-7.164-16-16-16 S0,7.164,0,16s7.164,16,16,16h8v16h-8l0,0l0,0C7.164,48,0,55.164,0,64s7.164,16,16,16c8.837,0,16-7.164,16-16l0,0v-8h16v7.98 c0,0.008-0.001,0.014-0.001,0.02c0,8.836,7.164,16,16,16s16-7.164,16-16S72.836,48.002,64,48z M64,8c4.418,0,8,3.582,8,8 s-3.582,8-8,8h-8v-8C56,11.582,59.582,8,64,8z M8,16c0-4.418,3.582-8,8-8s8,3.582,8,8v8h-8C11.582,24,8,20.417,8,16z M16,72 c-4.418,0-8-3.582-8-8s3.582-8,8-8l0,0h8v8C24,68.418,20.418,72,16,72z M32,48V32h16v16H32z M64,72c-4.418,0-8-3.582-8-8l0,0v-8 h7.999c4.418,0,8,3.582,8,8S68.418,72,64,72z">
                                    </path>
                                </g>
                            </g>
                        </svg>
                        Visit Now
                    </button>
                </a>
            </div>
        </div>
    </center>
</template>

<style scoped>
.card {
    width: 100%;
    height: 400px;
    background-color: #4158D0;
    background-image: linear-gradient(43deg, #4158D0 0%, #C850C0 46%, #FFCC70 100%);
    border-radius: 8px;
    color: white;
    overflow: hidden;
    position: relative;
    transform-style: preserve-3d;
    perspective: 1000px;
    transition: all 0.5s cubic-bezier(0.23, 1, 0.320, 1);
    cursor: pointer;
}

.card-content {
    padding: 20px;
    position: relative;
    z-index: 1;
    display: flex;
    flex-direction: column;
    gap: 10px;
    color: white;
    align-items: center;
    justify-content: center;
    text-align: center;
    height: 100%;
}

.card-content .card-title {
    font-size: 40px;
    font-weight: 700;
    color: inherit;
    text-transform: uppercase;
}

.card-content .card-para {
    color: inherit;
    opacity: 0.8;
    font-size: 18px;
}

.card:hover {
    transform: rotateY(10deg) rotateX(10deg) scale(1.05);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.card:before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.1));
    transition: transform 0.5s cubic-bezier(0.23, 1, 0.320, 1);
    z-index: 1;
}

.card:hover:before {
    transform: translateX(-100%);
}

.card:after {
    content: "";
    position: absolute;
    top: 0;
    right: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.1));
    transition: transform 0.5s cubic-bezier(0.23, 1, 0.320, 1);
    z-index: 1;
}

.card:hover:after {
    transform: translateX(100%);
}

.button {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: space-between;
    background-color: #000;
    color: #fff;
    font-size: 13px;
    border: 0.5px solid rgba(0, 0, 0, 0.1);
    padding-bottom: 8px;
    height: 65px;
    padding: 12px;
    border-radius: 15px 15px 12px 12px;
    cursor: pointer;
    position: relative;
    will-change: transform;
    transition: all .1s ease-in-out 0s;
    user-select: none;
    background-image: linear-gradient(to right, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0)),
        linear-gradient(to bottom, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0));
    background-position: bottom right, bottom right;
    background-size: 100% 100%, 100% 100%;
    background-repeat: no-repeat;
    box-shadow: inset -4px -10px 0px rgba(255, 255, 255, 0.4),
        inset -4px -8px 0px rgba(0, 0, 0, 0.3),
        0px 2px 1px rgba(0, 0, 0, 0.3),
        0px 2px 1px rgba(255, 255, 255, 0.1);
    transform: perspective(70px) rotateX(5deg) rotateY(0deg);
}

.button::after {
    content: '';
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0.2), rgba(0, 0, 0, 0.5));
    z-index: -1;
    border-radius: 15px;
    box-shadow: inset 4px 0px 0px rgba(255, 255, 255, 0.1),
        inset 4px -8px 0px rgba(0, 0, 0, 0.3);
    transition: all .1s ease-in-out 0s;
}

.button::before {
    content: '';
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-image: linear-gradient(to right, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0)),
        linear-gradient(to bottom, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0));
    background-position: bottom right, bottom right;
    background-size: 100% 100%, 100% 100%;
    background-repeat: no-repeat;
    z-index: -1;
    border-radius: 15px;
    transition: all .1s ease-in-out 0s;
}

.button:active {
    will-change: transform;
    transform: perspective(80px) rotateX(5deg) rotateY(1deg) translateY(3px) scale(0.96);
    height: 64px;
    border: 0.25px solid rgba(0, 0, 0, 0.2);
    box-shadow: inset -4px -8px 0px rgba(255, 255, 255, 0.2),
        inset -4px -6px 0px rgba(0, 0, 0, 0.8),
        0px 1px 0px rgba(0, 0, 0, 0.9),
        0px 1px 0px rgba(255, 255, 255, 0.2);
    transition: all .1s ease-in-out 0s;
}

.button::after:active {
    background-image: linear-gradient(to bottom, rgba(0, 0, 0, 0.5), rgba(255, 255, 255, 0.2));
}

.button:active::before {
    content: "";
    display: block;
    position: absolute;
    top: 5%;
    left: 20%;
    width: 50%;
    height: 80%;
    background-color: rgba(255, 255, 255, 0.1);
    animation: overlay 0.1s ease-in-out 0s;
    pointer-events: none;
}

.button svg {
    width: 15px;
    height: 15px;
}

@keyframes overlay {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

.button:focus {
    outline: none;
}
</style>
