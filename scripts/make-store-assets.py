from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
out=Path('docs/store/assets');out.mkdir(parents=True,exist_ok=True)
ink='#202E2A';green='#267263';paper='#F7F6F0'
def mark(draw,x,y,size):
    scale=size/108
    def box(coords): return tuple(round(v*scale+(x if i%2==0 else y)) for i,v in enumerate(coords))
    draw.rectangle(box((0,0,108,108)),fill=green)
    draw.rectangle(box((30,24,78,84)),fill=paper)
    for yy,end in [(40,69),(51,69),(62,59)]:
        draw.line(box((39,yy,end,yy)),fill=green,width=max(1,round(4*scale)))
icon=Image.new('RGB',(512,512),green);mark(ImageDraw.Draw(icon),0,0,512);icon.save(out/'icon-512.png')
im=Image.new('RGB',(1024,500),paper);d=ImageDraw.Draw(im)
mark(d,80,140,220)
font='C:/Windows/Fonts/YuGothB.ttc'
d.text((354,135),'墨メモ',font=ImageFont.truetype(font,82),fill=ink)
d.text((360,252),'ひらめきを、手元に。',font=ImageFont.truetype(font,32),fill=green)
d.text((360,312),'通信なしで使えるメモ帳',font=ImageFont.truetype(font,24),fill=ink)
im.save(out/'feature-1024x500.png')
